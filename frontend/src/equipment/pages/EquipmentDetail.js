import React from 'react';
import { useParams } from 'react-router-dom';
import api from '../../api';

const EquipmentDetail = () => {
  const { id } = useParams();
  const [equipment, setEquipment] = React.useState(null);

  React.useEffect(() => {
    const fetchEquipment = async () => {
      try {
        const { data } = await api.get(`/api/equipments/${id}`);
        setEquipment(data);
      } catch (error) {
        console.error('获取设备详情失败', error);
      }
    };
    fetchEquipment();
  }, [id]);

  if (!equipment) return <div>加载中...</div>;

  return (
    <div>
      <h2>设备详情</h2>
      <p>ID: {equipment.id}</p>
      <p>名称: {equipment.name}</p>
      <p>型号: {equipment.model}</p>
      <p>状态: {equipment.status}</p>
    </div>
  );
};

export default EquipmentDetail;