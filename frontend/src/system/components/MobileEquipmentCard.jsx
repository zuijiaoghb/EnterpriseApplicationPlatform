import { useBreakpoint } from 'antd';

const MobileEquipmentCard = ({ equipment }) => {
  const screens = useBreakpoint();
  
  return (
    <Card 
      style={{ margin: screens.xs ? 8 : 16 }}
      actions={[
        <ScanOutlined onClick={handleQRScan} />,
        <Tooltip title="快速报修">
          <WarningOutlined onClick={showRepairModal} />
        </Tooltip>
      ]}>
      <Descriptions column={1}>
        <Descriptions.Item label="运行状态">
          <StatusBadge status={equipment.status} />
        </Descriptions.Item>
        {/* ...其他设备信息... */}
      </Descriptions>
    </Card>
  );
};