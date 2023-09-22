import { UserProfile } from './userprofile.interface';

export interface UserResponse {
  content: UserProfile[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
  };
}
