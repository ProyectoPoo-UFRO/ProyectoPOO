export default function MainLayout({ children }) {
    return (
        <div className="min-h-screen bg-gray-50">
            <header className="p-4 bg-blue-600 text-white font-bold text-xl">
                Simulador Máquina Expendedora
            </header>

            <main className="p-6">{children}</main>
        </div>
    );
}
