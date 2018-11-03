export class User {
  public id: string;
  public email: string;
  public firstname: string;
  public lastname: string;
  public roles: any[];

  constructor(id: string, email: string, firstname: string, lastname: string, roles: any[]) {
    this.id = id;
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roles = roles;
  }
}
