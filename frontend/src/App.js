import React, { useState, useEffect } from "react";
import { fetchTestMessage } from "./api";

function App() {
    const [message, setMessage] = useState("Loading...");

    useEffect(() => {
        fetchTestMessage().then((data) => setMessage(data.message));
    }, []);

    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>Test Backend Connection</h1>
            <p>{message}</p>
        </div>
    );
}

export default App;