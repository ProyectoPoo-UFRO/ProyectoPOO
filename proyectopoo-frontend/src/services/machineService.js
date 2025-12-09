const API_URL = "http://localhost:8081/api/v1";
const BASE_IMG_URL = "http://localhost:8081";

const getAuthHeaders = () => {
    try {
        const userJson = localStorage.getItem("vending_user");
        if (!userJson) return { 'Content-Type': 'application/json' };

        const user = JSON.parse(userJson);
        let token = user?.token;

        if (token) {
            token = token.replace(/^"|"$/g, '');
            return {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            };
        }
    } catch (e) {
        console.error("Error leyendo sesión local:", e);
    }
    return { 'Content-Type': 'application/json' };
};

const resolveImageUrl = (imagePath) => {
    if (!imagePath) return "/img/logo-can.png";
    if (imagePath.startsWith("http")) return imagePath;

    const cleanPath = imagePath.startsWith("/") ? imagePath.slice(1) : imagePath;
    return `${BASE_IMG_URL}/${cleanPath}`;
};

export const getAllMachines = async () => {
    try {
        const headers = getAuthHeaders();
        const [machinesRes, latasRes] = await Promise.all([
            fetch(`${API_URL}/maquinas`, { headers }),
            fetch(`${API_URL}/latas`, { headers })
        ]);

        if (!machinesRes.ok || !latasRes.ok) throw new Error("Error obteniendo datos del backend");

        const machinesData = await machinesRes.json();
        const latasData = await latasRes.json();

        return machinesData.map((machine, index) => {
            const machineId = machine._id || machine.id || `temp-${index}`;

            let locationText = "Ubicación desconocida";
            if (machine.ubicacion) {
                locationText = typeof machine.ubicacion === 'string'
                    ? machine.ubicacion
                    : `Lat: ${machine.ubicacion.latitud || 0}, Long: ${machine.ubicacion.longitud || 0}`;
            }

            const enrichedProducts = (machine.productos || []).map(prod => {
                const infoLata = latasData.find(l => (l._id || l.id) === prod.lataId);
                return {
                    id: prod.lataId,
                    stock: prod.stock,
                    name: infoLata?.nombre || "Producto Desconocido",
                    price: infoLata?.precio || 0,
                    image: resolveImageUrl(infoLata?.imagen)
                };
            });

            return {
                id: machineId,
                name: machine.nombre || `Máquina ${index + 1}`,
                status: machine.estado || "OPERATIVA",
                location: locationText,
                stockMaximo: machine.stockMaximo || 0,
                products: enrichedProducts
            };
        });
    } catch (error) {
        console.error("Error en getAllMachines:", error);
        return [];
    }
};

export const updateMachineStatus = async (machineId, newStatus) => {
    try {
        const headers = getAuthHeaders();

        if (!headers.Authorization) {
            alert("Error: Sesión inválida. Por favor reinicie sesión.");
            return false;
        }

        const response = await fetch(`${API_URL}/maquinas/maquina/${machineId}/estado`, {
            method: 'POST',
            headers,
            body: JSON.stringify({ estado: newStatus })
        });

        if (!response.ok) {
            console.error(`Error API Estado: ${response.status}`);
        }
        return response.ok;
    } catch (error) {
        console.error("Error en updateMachineStatus:", error);
        return false;
    }
};

export const saveProductUpdate = async (machineId, product) => {
    try {
        const headers = getAuthHeaders();

        const stockRes = await fetch(`${API_URL}/maquinas/${machineId}/stock`, {
            method: 'PUT',
            headers,
            body: JSON.stringify({
                lataId: product.id,
                newStock: product.stock
            })
        });

        try {
            await fetch(`${API_URL}/latas/${product.id}/precio`, {
                method: 'PATCH',
                headers,
                body: JSON.stringify(product.price)
            });
        } catch (e) {
            console.warn("No se pudo sincronizar el precio global");
        }

        return stockRes.ok;
    } catch (error) {
        console.error("Error en saveProductUpdate:", error);
        return false;
    }
};

export const createProduct = async (machineId, productData) => {
    try {
        const headers = getAuthHeaders();

        const lataResponse = await fetch(`${API_URL}/latas`, {
            method: 'POST',
            headers,
            body: JSON.stringify({
                nombre: productData.name,
                precio: productData.price,
                imagen: productData.image || "images/latas/default.png"
            })
        });

        if (!lataResponse.ok) throw new Error("Fallo al crear Lata base");

        const newLata = await lataResponse.json();
        const newLataId = newLata.id || newLata._id;

        const assignResponse = await fetch(`${API_URL}/maquinas/${machineId}/productos`, {
            method: 'POST',
            headers,
            body: JSON.stringify({
                lataId: newLataId,
                stock: productData.stock
            })
        });

        if (!assignResponse.ok) throw new Error("Fallo al asignar producto a máquina");

        return { ...productData, id: newLataId };
    } catch (error) {
        console.error("Error en createProduct:", error);
        return null;
    }
};

export const deleteProduct = async (machineId, productId) => {
    try {
        const response = await fetch(`${API_URL}/maquinas/${machineId}/productos/${productId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        return response.ok;
    } catch (error) {
        console.error("Error en deleteProduct:", error);
        return false;
    }
};

export const registerSale = async (saleData) => {
    try {
        const response = await fetch(`${API_URL}/ventas`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(saleData)
        });

        if (!response.ok) return null;
        return await response.json();
    } catch (error) {
        console.error("Error en registerSale:", error);
        return null;
    }
};