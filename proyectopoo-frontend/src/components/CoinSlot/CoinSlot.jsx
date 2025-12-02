import { useContext } from "react";
import { VendingContext } from "../../context/VendingContext";
import "./CoinSlot.module.css";

export default function CoinSlot() {
    const { addMoney } = useContext(VendingContext);

    return (
        <div className="coin-slot">
            <button onClick={() => addMoney(100)}>+ $100</button>
            <button onClick={() => addMoney(500)}>+ $500</button>
            <button onClick={() => addMoney(1000)}>+ $1000</button>
        </div>
    );
}

