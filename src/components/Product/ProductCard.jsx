export default function ProductCard({ name, price, image }) {
    return (
        <div className="border rounded-xl p-4 shadow-sm hover:shadow-md transition">
            <img src={image} alt={name} className="w-full h-32 object-cover rounded-lg mb-2" />

            <h3 className="font-semibold text-lg">{name}</h3>
            <p className="text-gray-600">Precio: ${price}</p>

            <button className="mt-3 w-full bg-blue-600 text-white py-1.5 rounded-lg hover:bg-blue-700 transition">
                Comprar
            </button>
        </div>
    );
}
