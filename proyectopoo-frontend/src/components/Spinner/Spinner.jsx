import styles from './Spinner.module.css';

export default function Spinner() {
    return (
        <div className={styles.overlay}>
            <div className={styles.loader}></div>
            <span className={styles.text}>Cargando Sistema...</span>
        </div>
    );
}