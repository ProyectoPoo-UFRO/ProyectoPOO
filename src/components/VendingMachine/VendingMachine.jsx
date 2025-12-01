import ProductList from "../Product/ProductList";

export default function VendingMachine() {
    return (
        <div className="w-full max-w-lg mx-auto p-4 bg-white shadow-lg rounded-xl">
            <h2 className="text-2xl font-bold text-center mb-4">
                Máquina Expendedora
            </h2>

            <ProductList />
        </div>
    );
}
