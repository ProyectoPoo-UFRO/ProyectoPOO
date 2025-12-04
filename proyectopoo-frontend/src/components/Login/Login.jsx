import { useState } from "react";
import { useUser } from "../../context/UserContext";
import { Navigate } from "react-router-dom";
import styles from "./Login.module.css";

export default function Login() {
    const { login, user } = useUser();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    if (user) {
        return <Navigate to={user.role === 'admin' ? "/admin" : "/home"} replace />;
    }

    function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (!username.trim() || !password.trim()) {
            setError("Por favor complete todos los campos.");
            return;
        }

        const isSuccess = login(username, password);

        if (!isSuccess) {
            setError("Usuario o contraseña incorrectos");
        }
    }

    return (
        <div className={styles.splitScreen}>

            {/* SECCIÓN IZQUIERDA: EL FORMULARIO */}
            <div className={styles.leftPane}>
                <div className={styles.card}>
                    <div className={styles.logoContainer}>
                        {/* Esta sigue siendo tu imagen pequeña actual */}
                        <img
                            src="/img/logo-circle.png"
                            alt="LatApp Logo"
                            className={styles.logoImage}
                        />
                    </div>
                    <h1 className={styles.title}>LatApp Login</h1>

                    <form onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <label className={styles.label}>Usuario</label>
                            <input
                                className={styles.input}
                                type="text"
                                placeholder="Ingrese su usuario"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label className={styles.label}>Contraseña</label>
                            <input
                                className={styles.input}
                                type="password"
                                placeholder="••••••••"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        {error && <div className={styles.errorMessage}>⚠️ {error}</div>}
                        <button type="submit" className={styles.button}>
                            Ingresar
                        </button>
                    </form>
                </div>
            </div>

            {/* SECCIÓN DERECHA: IMAGEN GIGANTE */}
            <div className={styles.rightPane}>
                <div className={styles.heroContent}>
                    {/* CAMBIO AQUÍ: Usamos la nueva imagen de la argolla */}
                    <img
                        src="/img/logo-hero.png"
                        alt="LatApp Giant Logo"
                        className={styles.heroImage}
                    />
                </div>
            </div>

        </div>
    );
}