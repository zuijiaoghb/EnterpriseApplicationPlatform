const { getDefaultConfig } = require('@react-native/metro-config');

const defaultConfig = getDefaultConfig(__dirname);

const config = {
  ...defaultConfig,
  transformer: {
    babelTransformerPath: require.resolve('react-native-svg-transformer'),
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true,
      },
    }),
    minifierPath: require.resolve('metro-minify-terser'),
  },
  resolver: {
    ...defaultConfig.resolver,
    sourceExts: [...defaultConfig.resolver.sourceExts, 'json'],
    assetExts: [...defaultConfig.resolver.assetExts, 'jpg', 'png', 'ttf', 'otf'],
    extraNodeModules: {
      'missing-asset-registry-path': require.resolve('react-native/Libraries/Image/AssetRegistry'),
      '@react-native-vector-icons': require.resolve('react-native-vector-icons'),
      '@react-native-vector-icons/get-image': require.resolve('react-native-vector-icons'),
    },
  },
};

module.exports = config;