// Centralized date formatting utility for SQLite
// Always use UTC for consistency

export function formatDateForSQLite(date: Date): string {
    const pad = (n: number) => n < 10 ? '0' + n : n;
    return `${date.getUTCFullYear()}-${pad(date.getUTCMonth() + 1)}-${pad(date.getUTCDate())} ` +
           `${pad(date.getUTCHours())}:${pad(date.getUTCMinutes())}:${pad(date.getUTCSeconds())}`;
}

// Optionally, parse a SQLite DATETIME string to a JS Date (UTC)
export function parseSQLiteDate(dateStr: string): Date {
    // Expects 'YYYY-MM-DD HH:MM:SS'
    return new Date(dateStr.replace(' ', 'T') + 'Z');
}
