export function notifyAdminExpiry(userId: number | null, extinguisherId: number | null, expiryDate: string) {

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
                    Alert Admin ⚠️
                </h1>

                <p style="font-size: 16px; color: #333;">
                    ${userId}'s fire extinguisher (ID: ${extinguisherId ?? 'N/A'}) has expired.
                </p>

                <p style="font-size: 16px; color: #333;">
                    Please reach out to the user to address this issue.
                </p>

                <p style="font-size: 16px; color: #333;">
                    The due date and time was ${expiryDate}
                </p>

            </div>

        </body>
    </html>

    `;
}