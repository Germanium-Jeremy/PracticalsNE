export function welcomeEmailTemplate(username: string) {

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

                <h1 style="color: #2563eb;">
                    Welcome ${username} 👋
                </h1>

                <p style="font-size: 16px; color: #333;">
                    Thank you for registering.
                </p>

                <p style="font-size: 16px; color: #333;">
                    Your account has been successfully created.
                </p>

                <a 
                    href="http://localhost:5173/login"
                    style="
                        display: inline-block;
                        margin-top: 20px;
                        padding: 12px 20px;
                        background-color: #2563eb;
                        color: white;
                        text-decoration: none;
                        border-radius: 6px;
                    "
                >
                    Login
                </a>

            </div>

        </body>
    </html>

    `;
}