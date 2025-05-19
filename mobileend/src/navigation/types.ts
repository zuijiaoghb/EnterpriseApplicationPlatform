import { StackNavigationProp } from '@react-navigation/stack';

export type RootStackParamList = {
  Dashboard: undefined;
  Login: undefined;
  Main: undefined;
  // 添加其他屏幕的路由参数类型
};

export type LoginScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Login'>;