export interface Data {
  accessToken: string;
  user: {
    id: number;
    name: string;
    username: string;
    lastName: string;
    email: string;
    password: string;
    address: Address;
  };
}

export interface Address {
  via: string;
  numeroCivico: number;
  localita: string;
  cap: string;
  comune: string;
}
