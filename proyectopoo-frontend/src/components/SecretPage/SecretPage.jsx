import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../../context/UserContext";
import styles from "./SecretPage.module.css";

export default function SecretPage() {
    const canvasRef = useRef(null);
    const navigate = useNavigate();
    const { logout } = useUser();

    const [message, setMessage] = useState("Inicializando...");
    const [progress, setProgress] = useState(0);
    const [denied, setDenied] = useState(false);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");

        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;

        const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789@#$%^&*()*&^%";
        const fontSize = 16;
        const columns = canvas.width / fontSize;
        const drops = Array(Math.floor(columns)).fill(1);

        const draw = () => {
            ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
            ctx.fillRect(0, 0, canvas.width, canvas.height);

            ctx.fillStyle = denied ? "#ff003c" : "#06b6d4";
            ctx.font = `${fontSize}px monospace`;

            for (let i = 0; i < drops.length; i++) {
                const text = letters.charAt(Math.floor(Math.random() * letters.length));
                ctx.fillText(text, i * fontSize, drops[i] * fontSize);

                if (drops[i] * fontSize > canvas.height && Math.random() > 0.975) {
                    drops[i] = 0;
                }
                drops[i]++;
            }
        };

        const interval = setInterval(draw, 33);
        return () => clearInterval(interval);
    }, [denied]);

    useEffect(() => {
        const timeline = [
            { time: 1000, msg: "Accediendo al Núcleo...", prog: 10 },
            { time: 2500, msg: "Rompiendo Firewall...", prog: 35 },
            { time: 4000, msg: "Descargando Latas Infinitas...", prog: 60 },
            { time: 5500, msg: "Inyectando Saldo Ilimitado...", prog: 85 },
            { time: 7000, msg: "ACCESO SUPREMO...", prog: 99 },
            { time: 8500, msg: "", prog: 100, error: true },
        ];

        timeline.forEach(({ time, msg, prog, error }) => {
            setTimeout(() => {
                if (error) {
                    setDenied(true);
                    setTimeout(() => {
                        logout();
                        navigate("/");
                    }, 4000);
                } else {
                    setMessage(msg);
                    setProgress(prog);
                }
            }, time);
        });

    }, [navigate, logout]);

    return (
        <div className={styles.container}>
            <canvas ref={canvasRef} className={styles.canvas}></canvas>

            <div className={`${styles.overlay} ${denied ? styles.overlayError : ''}`}>
                {!denied ? (
                    <>
                        <div className={styles.message}>{message}</div>
                        <div className={styles.progressBar}>
                            <div
                                className={styles.progressFill}
                                style={{ width: `${progress}%` }}
                            ></div>
                        </div>
                        <p style={{color: '#0f0', marginTop: '10px', fontSize: '0.8rem'}}>
                            {progress}% COMPLETADO
                        </p>
                    </>
                ) : (
                    <>
                        <div className={styles.accessDenied}>
                            ACCESO DENEGADO
                        </div>
                        <div className={styles.godMessage}>
                            NO TIENES PERMISOS DE DIOS
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}