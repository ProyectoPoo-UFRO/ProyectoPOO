import ProductCard from "./ProductCard";

export default function ProductList() {
    const productos = [
        { name: "Coca-Cola", price: 1200, image: "/images/cocacola.jpg" },
        { name: "Snickers", price: 800, image: "/images/snickers.jpg" },
        { name: "Agua", price: 1000, image: "/images/water.jpg" },
    ];

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {productos.map((p, i) => (
                <ProductCard key={i} {...p} />
            ))}
        </div>
    );
}
