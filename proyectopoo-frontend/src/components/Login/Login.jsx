import { useState } from "react";
import { useUser } from "../../context/UserContext";
import { Navigate } from "react-router-dom";
import styles from "./Login.module.css";

export default function Login() {
    const { login, user } = useUser();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    if (user) {
        return <Navigate to={user.role === 'admin' ? "/admin" : "/home"} replace />;
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (!username.trim() || !password.trim()) {
            setError("Por favor complete todos los campos.");
            return;
        }

        const isSuccess = await login(username, password);

        if (!isSuccess) {
            setError("Usuario o contraseña incorrectos");
        }
    }

    return (
        <div className={styles.splitScreen}>

            <div className={styles.leftPane}>
                <div className={styles.card}>
                    <div className={styles.logoContainer}>
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
                            <div className={styles.passwordWrapper}>
                                <input
                                    className={styles.input}
                                    type={showPassword ? "text" : "password"} // Cambio dinámico
                                    placeholder="••••••••"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    style={{ paddingRight: '45px' }}
                                />
                                <button
                                    type="button"
                                    className={styles.eyeBtn}
                                    onClick={() => setShowPassword(!showPassword)}
                                    tabIndex="-1"
                                >
                                    {showPassword ? (
                                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                    ) : (
                                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>
                                    )}
                                </button>
                            </div>
                        </div>

                        {error && <div className={styles.errorMessage}>⚠️ {error}</div>}

                        <button type="submit" className={styles.button}>
                            Ingresar
                        </button>
                    </form>
                </div>
            </div>

            <div className={styles.rightPane}>
                <div className={styles.heroContent}>
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