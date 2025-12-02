// SIMULACIÓN DE BASE DE DATOS (Moveremos aquí tus datos hardcodeados)
// Cuando tengas Java, borrarás esta variable y usarás fetch()

const MOCK_DATA = [
    {
        id: 1,
        name: "Máquina Hall Central",
        location: "Edificio A - Piso 1",
        products: [
            { id: 101, name: "Coca-Cola", price: 1200, stock: 5, image: "https://http2.mlstatic.com/D_NQ_NP_727878-MLC50172087566_062022-O.webp" },
            { id: 102, name: "Pepsi", price: 1100, stock: 3, image: "https://i5.walmartimages.com/asr/3910f545-207a-4411-a83d-3d4dfa6665a0.2b6d57741d45903b7470557342d453b3.jpeg" },
            { id: 103, name: "Agua Mineral", price: 800, stock: 10, image: "https://jumbo.vtexassets.com/arquivos/ids/447547/Agua-mineral-sin-gas-16-L.jpg?v=637599602564200000" }
        ]
    },
    {
        id: 2,
        name: "Máquina Cafetería",
        location: "Edificio B - Comedor",
        products: [
            { id: 201, name: "Súper 8", price: 500, stock: 20, image: "https://images.tottus.cl/m/20002875_1.jpg" },
            { id: 202, name: "Galletas Gullón", price: 1500, stock: 8, image: "https://jumbo.vtexassets.com/arquivos/ids/410657/Galletas-digestive-avena-y-choc-265-g.jpg?v=637469344234000000" },
            { id: 203, name: "Jugo Naranja", price: 1000, stock: 0, image: "https://copuchas.cl/wp-content/uploads/2021/08/watts-naranja-15-litros.jpg" }
        ]
    }
];

// --- FUNCIONES DEL SERVICIO ---
// Todas son 'async' para simular que vienen de internet (Promise)

export const getAllMachines = async () => {
    // Simulamos un pequeño retraso de red (50ms)
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve(MOCK_DATA);
        }, 50);
    });
};

// En el futuro, aquí harás: return fetch('/api/machines', { method: 'POST'... })
export const saveProductUpdate = async (machineId, product) => {
    console.log(`[API FAKE] Actualizando producto ${product.id} en máquina ${machineId}`);
    return Promise.resolve(true); // Retornamos éxito
};

export const createProduct = async (machineId, productData) => {
    console.log(`[API FAKE] Creando producto en máquina ${machineId}`, productData);
    // Simulamos que la BD nos devuelve el objeto creado con un ID real
    return Promise.resolve({ ...productData, id: Date.now() });
};