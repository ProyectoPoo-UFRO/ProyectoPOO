import { useState } from "react";

export default function useVendingMachine() {
    const [balance, setBalance] = useState(0);

    function addMoney(amount) {
        setBalance((prev) => prev + amount);
    }

    function resetBalance() {
        setBalance(0);
    }

    return {
        balance,
        addMoney,
        resetBalance,
    };
}
