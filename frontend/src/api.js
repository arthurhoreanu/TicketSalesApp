const API_URL = "http://localhost:8080/api";

export const fetchTestMessage = async () => {
    try {
        const response = await fetch(`${API_URL}/test`);
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching data:", error);
        return { message: "Error fetching data" };
    }
};
