import { getAllUsers, getUserByEmail, getUserById, getUserByRole } from "../database/repositories/user.repo.js";

export async function getUsers() {
    const users = await getAllUsers();
    return users;
}

export async function getAUser(field: string, value: string) {
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