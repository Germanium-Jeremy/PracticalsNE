import { addFireExtinguisher, getAllFireExtinguishers, getNumberOfExtenguishers, buyExtinguisher, returnExtinguisher } from "../database/repositories/fe.repo.js";
import { getUserById } from "../database/repositories/user.repo.js";
import { formatDateForSQLite } from "../utils/date.js";

export async function service_getAllExtinguishers() {
    const extinguishers = await getAllFireExtinguishers();
    const count = await getNumberOfExtenguishers();
    return { extinguishers, count };
}

export async function service_addExtinguisher() {
    return await addFireExtinguisher();
}

export async function service_buyExtinguisher(extinguisherId: number, userId: number) {
    const user = await getUserById(userId);
    if (!user) throw new Error("User not found");
    const now = new Date();
    const boughtAt = formatDateForSQLite(now);
    // For testing, set expiresAt to 2 minutes from now (UTC)
    const expiresAt = formatDateForSQLite(new Date(now.getTime() + 2 * 60 * 1000));
    return await buyExtinguisher(extinguisherId, userId, boughtAt, expiresAt);
}

// Auto-assign a new extinguisher to user on return
export async function service_returnAndAutoAssignExtinguisher(extinguisherId: number, userId: number) {
    // Mark the old extinguisher as returned
    const now = new Date();
    const returnedAt = formatDateForSQLite(now);
    await returnExtinguisher(extinguisherId, returnedAt);
    // Find a random available extinguisher
    const extinguishers: any = await getAllFireExtinguishers();
    const available = extinguishers.filter((ex: any) => ex.status === 'in_stock');
    if (available.length === 0) {
        return { message: "Returned, but no available extinguisher to assign." };
    }
    const random = available[Math.floor(Math.random() * available.length)];
    // Assign it to the user
    const boughtAt = formatDateForSQLite(now);
    const expiresAt = formatDateForSQLite(new Date(now.getTime() + 2 * 60 * 1000));
    await buyExtinguisher(random.id, userId, boughtAt, expiresAt);
    return { message: "Returned and new extinguisher assigned", newExtinguisherId: random.id };
}

