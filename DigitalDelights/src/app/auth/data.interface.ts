export interface Data {
  accessToken: string;
  role: string;
  user: {
    id: number;
    name: string;
    username: string;
    lastName: string;
    email: string;
    password: string;
    address: Address;
    role: string;
  };
}

export interface Address {
  via: string;
  numeroCivico: number;
  localita: string;
  cap: string;
  comune: string;
}
