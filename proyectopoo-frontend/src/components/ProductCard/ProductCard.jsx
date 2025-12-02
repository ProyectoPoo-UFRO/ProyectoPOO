import { useContext } from "react";
import { VendingContext } from "../../context/VendingContext";
import "./ProductCard.css";

export default function Display() {
    const { displayMessage } = useContext(VendingContext);

    return <div className="display">{displayMessage}</div>;
}

