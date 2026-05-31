export function notifyExpiry(username: string, extinguisherId: number | null, expiryDate: string) {

    return `
    
    <html>
        <body style="
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 40px;
        ">
        
            <div style="
                max-width: 600px;
                margin: auto;
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            ">

                <h1 style="color: #aa3322;">
                    Alert ${username} ⚠️
                </h1>

                <p style="font-size: 16px; color: #333;">
                    Your fire extinguisher (ID: ${extinguisherId ?? 'N/A'}) is expiring soon.
                </p>

                <p style="font-size: 16px; color: #333;">
                    Please replace it as soon as possible.
                </p>

                <p style="font-size: 16px; color: #333;">
                    The due date and time is ${expiryDate}
                </p>

            </div>

        </body>
    </html>

    `;
}