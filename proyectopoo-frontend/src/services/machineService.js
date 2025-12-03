// src/services/machineService.js

// CONFIGURACIÓN: Puerto 8081
const API_URL = "http://localhost:8081/api/v1";

export const getAllMachines = async () => {
    try {
        console.log("📡 Cargando datos del sistema...");

        // 1. Pedimos las MÁQUINAS y las LATAS al mismo tiempo
        const [machinesRes, latasRes] = await Promise.all([
            fetch(`${API_URL}/maquinas`),
            fetch(`${API_URL}/latas`)
        ]);

        if (!machinesRes.ok || !latasRes.ok) {
            throw new Error("Error: Una de las peticiones al backend falló.");
        }

        // Convertimos a JSON
        const machinesData = await machinesRes.json();
        const latasData = await latasRes.json();

        console.log("📦 Máquinas crudas:", machinesData);
        console.log("🥫 Catálogo de Latas:", latasData);

        // 2. EL GRAN CRUCE DE DATOS (Mapping)
        const adaptedData = machinesData.map((machine, index) => {

            // Nombre de la máquina (Fallback si no viene)
            const machineName = machine.nombre || `Máquina ${index + 1} (${machine.estado})`;

            return {
                id: machine._id || `temp-${index}`,
                name: machineName,
                location: machine.ubicacion
                    ? `Lat: ${machine.ubicacion.latitud}, Long: ${machine.ubicacion.longitud}`
                    : "Ubicación desconocida",

                // 3. AQUÍ OCURRE LA MAGIA: Unimos Stock con Info de Lata
                products: (machine.productos || []).map(prod => {

                    // CORRECCIÓN AQUÍ: Usamos 'l.id' en lugar de 'l._id'
                    // (O soportamos ambos por si acaso cambia en el futuro)
                    const infoLata = Array.isArray(latasData)
                        ? latasData.find(l => (l.id || l._id) === prod.lataId)
                        : null;

                    if (!infoLata) {
                        console.warn(`⚠️ No se encontró info para lataId: ${prod.lataId}`);
                    }

                    return {
                        id: prod.lataId,
                        stock: prod.stock,

                        // Si encontramos la lata, usamos sus datos. Si no, mostramos el ID para depurar.
                        name: infoLata?.nombre || `ID: ${prod.lataId} (Sin Datos)`,
                        price: infoLata?.precio || 0,
                        image: infoLata?.imagen || "https://via.placeholder.com/150?text=Sin+Imagen"
                    };
                })
            };
        });

        return adaptedData;

    } catch (error) {
        console.error("🔥 Error cargando datos:", error);
        return [];
    }
};

// --- FUNCIONES DE ESCRITURA (Simuladas por ahora) ---

export const saveProductUpdate = async (machineId, product) => {
    // Aquí conectarás con PUT /ventas o similar en el futuro
    return new Promise(r => setTimeout(r, 500));
};

export const createProduct = async (machineId, productData) => {
    // Tu backend requiere crear una Lata primero. Por ahora simulamos.
    return new Promise(r => setTimeout(r, 500));
};

export const deleteProduct = async (machineId, productId) => {
    return new Promise(r => setTimeout(r, 500));
};