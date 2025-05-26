import { StackNavigationProp } from '@react-navigation/stack';

export type RootStackParamList = {
  Dashboard:  { param1: string; param2: number };
  Login:  { param1: string; param2: number };
  UserManagement:  { param1: string; param2: number };
  RoleManagement: { param1: string; param2: number };
  PermissionManagement:  { param1: string; param2: number };
  ClientManagement:  { param1: string; param2: number };
  Portal: undefined;
  EquipmentList: { param1: string; param2: number };
  SystemSettings: { param1: string; param2: number };
};

export type LoginScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Login'>;