import { getAllUsers, getUserByEmail, getUserById, getUserByRole } from "../database/repositories/user.repo.js";

export async function service_getUsers() {
    return await getAllUsers();
}

export async function service_getAUser(field: string, value: string) {
    let user;
    if (field === 'email') {
        user = await getUserByEmail(value);
    } else if (field === 'id') {
        user = await getUserById(parseInt(value));
    } else if (field === 'role') {
        user = await getUserByRole(value);
    } else {
        throw new Error("Invalid field");
    }
    return user;
}