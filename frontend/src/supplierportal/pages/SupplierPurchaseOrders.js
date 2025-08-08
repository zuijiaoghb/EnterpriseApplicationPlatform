import React, { useState, useEffect } from 'react';
import { Table, Card, Typography, Spin, message, Form, Input, DatePicker, Button, Divider, Empty, Space, Tooltip } from 'antd';
import { useNavigate } from 'react-router-dom';
import locale from 'antd/lib/date-picker/locale/zh_CN';
import api from '../../api';
import { SearchOutlined, ExportOutlined, FilterOutlined, CalendarOutlined } from '@ant-design/icons';
import { DownOutlined, UpOutlined } from '@ant-design/icons';
import { BarChartOutlined } from '@ant-design/icons';
import './SupplierPurchaseOrders.css'; // 引入新的样式文件

const { Title, Text } = Typography;

const SupplierPurchaseOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10); // 减小默认页码以提升性能
  const [total, setTotal] = useState(0);
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({});
  const [form] = Form.useForm();
  const [expandedRowKeys, setExpandedRowKeys] = useState([]);
  const [isAdvancedSearch, setIsAdvancedSearch] = useState(false);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        // 获取当前登录用户信息
        const userResponse = await api.get('/auth/info');
        const vendorCode = userResponse.data.username;

        if (!vendorCode) {
          message.error('无法获取供应商信息');
          return;
        }

        // 调用后端API获取供应商已审核的采购订单
        const response = await api.get('/api/inventory/purchase/vendor/audited-orders', {
          params: {
            vendorCode,
            pageNum: currentPage,
            pageSize,
            cPOID: searchParams.cPOID,
            dPODate: searchParams.dPODate,
            cInvCode: searchParams.cInvCode,
            cItemName: searchParams.cItemName
          }
        });
        setOrders(response.data.records);
        setTotal(response.data.total);
      } catch (error) {
        message.error('获取采购订单失败: ' + (error.response?.data?.message || error.message));
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [currentPage, pageSize, searchParams]);

  // 切换展开/折叠行
  const toggleExpand = (record) => {
    const rowKey = `${record.cPOID}_${record.irowno}`;
    if (expandedRowKeys.includes(rowKey)) {
      setExpandedRowKeys(expandedRowKeys.filter(key => key !== rowKey));
    } else {
      setExpandedRowKeys([...expandedRowKeys, rowKey]);
    }
  };

  // 导出数据
  const handleExport = () => {
    message.info('正在导出数据，请稍候...');
    // 实现导出逻辑
  };

  // 重置搜索条件
  const handleReset = () => {
    form.resetFields();
    setSearchParams({});
    setCurrentPage(1);
  };

  const handleSearch = (values) => {
    setSearchParams(values);
    setCurrentPage(1);
  };

  // 打印条码 - 调用HYBarCodeMainController接口
  const handlePrintBarcode = async (record) => {
    try {
      setLoading(true);
      
      // 构建条码数据
      const barcodeData = {
        cPOID: record.cPOID,
        dPODate: record.dPODate,
        cVenCode: record.cVenCode,
        cDefine1: record.cDefine1,
        cPersonCode: record.cPersonCode,
        cDepCode: record.cDepCode,
        cInvCode: record.cInvCode,
        cItemName: record.cItemName,
        iQuantity: record.iQuantity,
        cUnitID: record.cUnitID,
        unitName: record.unitName,
        dArriveDate: record.dArriveDate,
        remainingQuantity: record.remainingQuantity,
        receivedQuantity: record.receivedQuantity,
        batchNumber: record.batchNumber || '',
        irowno: record.irowno,
        supplierName: record.supplierName
      };

      // 调用HYBarCodeMainController接口保存条码
      const response = await api.post('/api/inventory/hy-barcode-main', barcodeData);
      
      if (response.status === 201) {
        message.success('条码生成成功，准备打印...');
        
        // 这里可以添加打印逻辑
        // 例如：调用浏览器打印API或生成打印预览
        console.log('生成的条码数据:', response.data);
        
        // 模拟打印操作
        setTimeout(() => {
          if (window.confirm(`条码已生成，条码号: ${response.data.barCode || '自动生成'}

是否立即打印？`)) {
            // 这里可以集成实际的打印功能
            window.print();
          }
        }, 500);
      }
      
    } catch (error) {
      console.error('条码生成失败:', error);
      message.error('条码生成失败: ' + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  // 表格列定义
  const columns = [
    {
      title: '订单编号',
      dataIndex: 'cPOID',
      key: 'cPOID',
      sorter: (a, b) => a.cPOID.localeCompare(b.cPOID),
      width: 120,
      fixed: 'left',
      render: (text) => <Text strong>{text}</Text>
    },
    {
      title: '订单日期',
      dataIndex: 'dPODate',
      key: 'dPODate',
      render: (date) => date ? new Date(date).toLocaleDateString() : '',
      sorter: (a, b) => new Date(a.dPODate) - new Date(b.dPODate),
    },
    {
      title: '供应商名称',
      dataIndex: 'supplierName',
      key: 'supplierName',
      sorter: (a, b) => a.supplierName.localeCompare(b.supplierName),
      width: 180
    },
    {
      title: '执行公司',
      dataIndex: 'cDefine1',
      key: 'cDefine1',
      sorter: (a, b) => a.cDefine1.localeCompare(b.cDefine1),
      width: 120
    },
    {
      title: '存货编码',
      dataIndex: 'cInvCode',
      key: 'cInvCode',
      sorter: (a, b) => a.cInvCode.localeCompare(b.cInvCode),
      width: 120
    },
    {
      title: '存货名称',
      dataIndex: 'cItemName',
      key: 'cItemName',
      sorter: (a, b) => a.cItemName.localeCompare(b.cItemName),
      width: 180
    },
    {
      title: '订单数量',
      dataIndex: 'iQuantity',
      key: 'iQuantity',
      render: (quantity) => (
        <div style={{ textAlign: 'right' }}>{(quantity ?? 0).toFixed(2)}</div>
      ),
      sorter: (a, b) => (a.iQuantity ?? 0) - (b.iQuantity ?? 0),
      width: 100
    },
    {
      title: '单位名称',
      dataIndex: 'unitName',
      key: 'unitName',
      sorter: (a, b) => a.unitName.localeCompare(b.unitName),
      width: 100
    },
    {
      title: '计划到货日期',
      dataIndex: 'dArriveDate',
      key: 'dArriveDate',
      render: (date) => date ? new Date(date).toLocaleDateString() : '',
      sorter: (a, b) => new Date(a.dArriveDate) - new Date(b.dArriveDate),
      width: 120
    },
    {
      title: '剩余未入库数量',
      dataIndex: 'remainingQuantity',
      key: 'remainingQuantity',
      render: (quantity) => (
        <div style={{ textAlign: 'right', fontWeight: quantity > 0 ? 'bold' : 'normal', color: quantity > 0 ? '#f5222d' : 'inherit' }}>
          {(quantity ?? 0).toFixed(2)}
        </div>
      ),
      sorter: (a, b) => (a.remainingQuantity ?? 0) - (b.remainingQuantity ?? 0),
      width: 120
    },
    {
      title: '已入库数量',
      dataIndex: 'receivedQuantity',
      key: 'receivedQuantity',
      render: (quantity) => (
        <div style={{ textAlign: 'right' }}>{(quantity ?? 0).toFixed(2)}</div>
      ),
      sorter: (a, b) => (a.receivedQuantity ?? 0) - (b.receivedQuantity ?? 0),
      width: 120
    },
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 120,
      render: (_, record) => (
        <Space size="middle">
          <Button 
            type="primary" 
            size="small"
            onClick={() => handlePrintBarcode(record)}
            disabled={record.remainingQuantity <= 0}
          >
            打印条码
          </Button>
          <Tooltip title={expandedRowKeys.includes(`${record.cPOID}_${record.irowno}`) ? '收起' : '展开'}>
            <Button
                icon={expandedRowKeys.includes(`${record.cPOID}_${record.irowno}`) ? <UpOutlined /> : <DownOutlined />}
                onClick={() => toggleExpand(record)}
                size="small"
              />
          </Tooltip>
        </Space>
      )
    }
  ];

  // 展开行内容 - 简化版本以便调试
  const expandedRowRender = (record) => (
    <div style={{ padding: '16px', border: '1px solid #f0f0f0', backgroundColor: '#fafafa' }}>
      <div style={{ marginBottom: '8px', fontWeight: 'bold' }}>订单详情</div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '12px' }}>
        <div><span style={{ color: '#999' }}>条码值:</span> {record.barcode || '-'}</div>
        <div><span style={{ color: '#999' }}>批号:</span> {record.batchNumber || '-'}</div>
          <div><span style={{ color: '#999' }}>订单行号:</span> {record.irowno || '-'}</div>
          <div><span style={{ color: '#999' }}>采购员:</span> {record.cPersonCode || '-'}</div>
          <div><span style={{ color: '#999' }}>供应商代码:</span> {record.cVenCode || '-'}</div>
          <div><span style={{ color: '#999' }}>采购部门:</span> {record.cDepCode || '-'}</div>
        <div><span style={{ color: '#999' }}>订单状态:</span> <span style={{ padding: '2px 8px', backgroundColor: '#52c41a', color: 'white', borderRadius: '4px', fontSize: '12px' }}>已审核</span></div>
      </div>
    </div>
  );

  return (
    <div className="supplier-purchase-orders-container">
      <div className="page-header">
        <div className="header-left">
          <BarChartOutlined className="page-icon" />
          <Title level={2} className="page-title">供应商采购订单管理</Title>
        </div>
        <div className="header-right">
          <Button
            type="primary"
            icon={<ExportOutlined />}
            onClick={handleExport}
            className="export-button"
          >
            导出数据
          </Button>
        </div>
      </div>

      <Card className="main-card" variant="outlined">
        <Spin spinning={loading} tip="加载采购订单..." className="custom-spin">
          <Form form={form} onFinish={handleSearch} layout="vertical">
            <div className="search-container">
              <div className="basic-search">
                <div className="search-row">
                  <Form.Item name="cPOID" label="订单编号" className="search-item">
                    <Input placeholder="请输入订单编号" prefix={<SearchOutlined className="search-icon" />} style={{ width: '100%', height: '51px' }} />
                  </Form.Item>
                  <Form.Item name="dPODate" label="订单日期" className="search-item">
                    <DatePicker placeholder="选择订单日期" format="YYYY-MM-DD" locale={locale} prefix={<CalendarOutlined className="search-icon" />} style={{ width: '100%', height: '51px' }} />
                  </Form.Item>
                  <Form.Item name="cInvCode" label="存货编码" className="search-item">
                    <Input placeholder="请输入存货编码" prefix={<SearchOutlined className="search-icon" />} style={{ width: '100%', height: '51px' }} />
                  </Form.Item>
                  <Form.Item name="cItemName" label="存货名称" className="search-item">
                    <Input placeholder="请输入存货名称" prefix={<SearchOutlined className="search-icon" />} style={{ width: '100%', height: '51px' }} />
                  </Form.Item>
                  <Form.Item className="search-button-group">
                    <Space>
                      <Button type="primary" htmlType="submit" className="search-button">
                        <SearchOutlined /> 搜索
                      </Button>
                      <Button onClick={handleReset} className="reset-button">
                        重置
                      </Button>
                      <Button
                        type="dashed"
                        onClick={() => setIsAdvancedSearch(!isAdvancedSearch)}
                        icon={<FilterOutlined />}
                        className="advanced-search-button"
                      >
                        {isAdvancedSearch ? '收起筛选' : '高级筛选'}
                      </Button>
                    </Space>
                  </Form.Item>
                </div>

                {isAdvancedSearch && (
                  <div className="advanced-search-content">
                    <Divider orientation="left" plain>高级搜索</Divider>
                    <div className="search-row">
                      <Form.Item name="cVenCode" label="供应商代码" className="search-item">
                        <Input placeholder="请输入供应商代码" />
                      </Form.Item>
                      <Form.Item name="cPersonCode" label="采购员" className="search-item">
                        <Input placeholder="请输入采购员" />
                      </Form.Item>
                      <Form.Item name="cDepCode" label="采购部门" className="search-item">
                        <Input placeholder="请输入采购部门" />
                      </Form.Item>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </Form>

          <div className="table-container">
            {orders.length === 0 && !loading ? (
              <Empty
                description="暂无采购订单数据"
                image={Empty.PRESENTED_IMAGE_SIMPLE}
                className="empty-state"
              />
            ) : (
              <Table
                columns={columns}
                dataSource={orders}
                pagination={{
                  current: currentPage,
                  pageSize: pageSize,
                  total: total,
                  onChange: (page, pageSize) => {
                    setCurrentPage(page);
                    setPageSize(pageSize);
                  },
                  showSizeChanger: true,
                  showTotal: (total) => `共 ${total} 条记录`,
                  className: "custom-pagination"
                }}
                rowKey={record => `${record.cPOID}_${record.irowno}`}
                tableLayout="auto"
                scroll={{ x: 'max-content', y: 'calc(100vh - 420px)' }}
                expandable={{
                  expandedRowRender: expandedRowRender,
                  expandedRowKeys: expandedRowKeys
                }}
                rowClassName={(record, index) => index % 2 === 0 ? 'even-row' : 'odd-row'}
                onRow={(record) => ({
                  onMouseEnter: () => {},
                  onMouseLeave: () => {},
                })}
                className="custom-table"
              />
            )}
          </div>
        </Spin>
      </Card>
    </div>
  );
};

export default SupplierPurchaseOrders;
