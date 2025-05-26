import { StackNavigationProp } from '@react-navigation/stack';

export type RootStackParamList = {
  Dashboard: undefined;
  Login: undefined;
  Main: undefined;
  UserManagement: undefined;
  RoleManagement: undefined;
  PermissionManagement: undefined;
  ClientManagement: undefined;
};

export type LoginScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Login'>;