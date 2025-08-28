import { useState, useEffect } from 'react';
import { Table, Card, Typography, Spin, message, Form, Input, DatePicker, Button, Divider, Empty, Space, Tooltip, QRCode } from 'antd';
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
          timeout: 30000,
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
        
        // 获取订单数据后，为每个订单获取总打印次数
        const ordersData = response.data.records;
        const ordersWithPrintCount = await Promise.all(
          ordersData.map(async (order) => {
            try {
              const printCountResponse = await api.get('/api/inventory/hy-barcode-main/total-print-count', {
                params: {
                  csrccode: order.cPOID,
                  csrcsubid: order.cSrcSubID
                }
              });
              return {
                ...order,
                totalPrintCount: printCountResponse.data.totalPrintCount || 0
              };
            } catch (error) {
              console.error(`获取订单 ${order.cPOID} 行 ${order.cSrcSubID} 的打印次数失败:`, error);
              return {
                ...order,
                totalPrintCount: 0
              };
            }
          })
        );
        
        setOrders(ordersWithPrintCount);
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

  // 导出数据 - 实现导出所有页数据的功能
  const handleExport = async () => {
    try {
      message.info('正在准备导出数据，请稍候...');
      
      // 获取当前登录用户信息
      const userResponse = await api.get('/auth/info');
      const vendorCode = userResponse.data.username;

      if (!vendorCode) {
        message.error('无法获取供应商信息');
        return;
      }

      // 创建导出参数，移除分页参数以获取所有数据
      const exportParams = {
        vendorCode,
        cPOID: searchParams.cPOID,
        dPODate: searchParams.dPODate,
        cInvCode: searchParams.cInvCode,
        cItemName: searchParams.cItemName,
        pageNum: 1, // 获取第一页开始
        pageSize: 10000 // 设置较大的页大小来获取所有数据
      };

      // 调用后端API获取所有数据
      const response = await api.get('/api/inventory/purchase/vendor/audited-orders', {
        timeout: 60000, // 设置更长的超时时间
        params: exportParams
      });

      if (response.data && response.data.records && response.data.records.length > 0) {
        const allData = response.data.records;
        
        // 处理数据格式
        const exportData = allData.map((item, index) => ({
          '序号': index + 1,
          '订单编号': item.cPOID || '',
          '订单日期': item.dPODate ? new Date(item.dPODate).toLocaleDateString() : '',
          '供应商代码': item.cVenCode || '',
          '供应商名称': item.supplierName || '',
          '存货编码': item.cInvCode || '',
          '存货名称': item.cItemName || '',
          '执行公司': item.cDefine1 || '',
          '采购数量': item.iQuantity ? parseFloat(item.iQuantity).toFixed(2) : '0.00',
          '已入库数量': item.receivedQuantity ? parseFloat(item.receivedQuantity).toFixed(2) : '0.00',
          '剩余未入库数量': item.remainingQuantity ? parseFloat(item.remainingQuantity).toFixed(2) : '0.00',
          '单位': item.unitName || '',
          '计划到货日期': item.dArriveDate ? new Date(item.dArriveDate).toLocaleDateString() : '',
          '采购员': item.personName || '',
          '采购部门': item.cDepCode || '',
          '订单行号': item.irowno || '',
          '条码值': item.barcode || '',
          '批号': item.batchNumber || ''
        }));

        // 创建CSV内容
        const headers = Object.keys(exportData[0]);
        const csvContent = [
          headers.join(','), // 表头
          ...exportData.map(row => 
            headers.map(header => {
              let value = row[header] || '';
              // 处理包含逗号或引号的字段
              if (typeof value === 'string' && (value.includes(',') || value.includes('"') || value.includes('\n'))) {
                value = `"${value.replace(/"/g, '""')}"`;
              }
              return value;
            }).join(',')
          )
        ].join('\n');

        // 创建Blob对象
        const blob = new Blob(['\ufeff' + csvContent], { 
          type: 'text/csv;charset=utf-8;' 
        });

        // 创建下载链接
        const link = document.createElement('a');
        const url = URL.createObjectURL(blob);
        
        // 设置文件名
        const currentDate = new Date().toISOString().split('T')[0];
        const fileName = `供应商采购订单_${currentDate}.csv`;
        
        link.href = url;
        link.setAttribute('download', fileName);
        link.style.display = 'none';
        
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // 释放URL对象
        URL.revokeObjectURL(url);
        
        message.success(`成功导出 ${exportData.length} 条数据到 ${fileName}`);
      } else {
        message.warning('当前查询条件下没有可导出的数据');
      }
    } catch (error) {
      console.error('导出数据失败:', error);
      message.error('导出数据失败: ' + (error.response?.data?.message || error.message));
    }
  };

  // 可选：导出为Excel格式的备用方法

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
      // 让用户输入条码数量
      const inputValue = prompt(`请输入条码数量（未入库量：${record.remainingQuantity} ${record.unitName}）`, record.remainingQuantity || 1);
      
      if (inputValue === null) {
        return; // 用户取消输入
      }

      const qty = parseFloat(inputValue);
      
      // 验证输入数量
      if (isNaN(qty) || qty <= 0) {
        message.error('请输入有效的条码数量（必须大于0）');
        return;
      }

      if (qty > record.remainingQuantity) {
        message.error(`条码数量不能超过未入库量（${record.remainingQuantity} ${record.unitName}）`);
        return;
      }

      setLoading(true);
      
      // 生成唯一条码值
      const timestamp = Date.now();
      console.log('timestamp:', timestamp);
      const randomStr = Math.random().toString(36).substring(2, 5).toUpperCase();
      console.log('randomStr:', randomStr);
      const barcode = `${record.cPOID}_${record.irowno}_${timestamp}_${randomStr}`;          
      
      // 获取当前日期（yyyy-MM-dd格式）
      const currentDate = new Date().toISOString().split('T')[0];
      console.log('currentDate:', currentDate);
      
      // 构建条码数据 - 严格按照后端HYBarCodeMain实体字段匹配
      const barcodeData = {
        barcode: barcode, // 设置条码值作为主键
        barCodeRule: '301', // 设置条码规则
        cInvCode: record.cInvCode,
        cVenCode: record.cVenCode,
        cWhCode: '', // 默认仓库代码
        cPosCode: '', // 默认货位代码
        iInvSaleCost: 0,
        dMdate: currentDate,
        dVdate: '', 
        iMassdate: 0,
        cMassUnit: '',
        cChkItemCode: '',
        cOther: '',
        cHoldItem: '_',
        cSHoldItem: '',
        cSilvItem: '',
        cFree1: '',
        cFree2: '',
        cFree3: '',
        cFree4: '',
        cFree5: '',
        cFree6: '',
        cFree7: '',
        cFree8: '',
        cFree9: '',
        cFree10: '',        
        qty: qty, // 使用用户输入的数量
        iNum: 0,
        pLot: currentDate,
        ichangerate: 0,
        cInvSN: '',
        cDefine1: '江西江特电机有限公司',
        cDefine2: '',
        cDefine3: '',
        cDefine4: '',
        cDefine5: '',
        cDefine6: '',
        cDefine7: '',
        cDefine8: '',
        cDefine9: '',
        cDefine10: '',
        cDefine11: '',
        cDefine12: '',
        cDefine13: '',
        cDefine14: '',
        cDefine15: '',
        cDefine16: '',
        cDefine22: record.cPOID,
        cDefine23: '',
        cDefine24: '',
        cDefine25: '',
        cDefine26: '',
        cDefine27: '',
        cDefine28: '',
        cDefine29: '',
        cDefine30: '',
        cDefine31: '',
        cDefine32: '',
        cDefine33: '',
        cDefine34: '',
        cDefine35: '',
        cDefine36: '',
        cDefine37: '',
        createDate: currentDate,
        createTime: new Date().toISOString(), // ISO格式，后端会自动解析
        dBusDate: currentDate,
        cBarcodeDefine1: '',
        cBarcodeDefine2: '',
        cBarcodeDefine3: '',
        cBarcodeDefine4: '',
        cBarcodeDefine5: '',
        cBarcodeDefine6: '',
        cBarcodeDefine7: '',
        cBarcodeDefine8: '',
        cBarcodeDefine9: '',
        cBarcodeDefine10: '',
        cComUnitCode: record.cUnitID,
        cComAddUnitCode:'',        
        cSrcCode: record.cPOID,        
        cSrcVouchType: '采购订单',
        cSrcSubID: record.cSrcSubID,
        cBarMainID: '',
        cBarMainAutoID: '',        
        cMaker: record.supplierName,        
        cGuid: '',
        cLabelCode: '5000',
        supBarCode:'',
        iPrtCount: 0,        
        iBarCodeState: 0, // 改为数字类型
        bExpSub: 0,
        cNoUseMaker: '',       
        dNoUseTime: null,
        bUseLs: 0,
        cInvBarCode: null,
        cinvcBarCode: null,
        cWhBarCode: null,
        cPosBarCode: null,
        cVenBarCode: null,
        cSaleUnitCode: '',
        iSaleQty: 0,
        iSalePrice: 0,
        iExpiratDateCalcu: 0,
        cExpirationdate: '',
        dExpirationdate: null,
        cBatchProperty1: '',
        cBatchProperty2: '',
        cBatchProperty3: '',
        cBatchProperty4: '',
        cBatchProperty5: '',
        cBatchProperty6: '',
        cBatchProperty7: '',
        cBatchProperty8: '',
        cBatchProperty9: '',
        cBatchProperty10: '',
        irowno: record.irowno || 0,
        iRelAutoid: 21,
        cCusCode: '',
        idemandtype: 0,
        cdemandcode: '',
        idemandseq: 0,
        cdemandid: '',
        clastscantype: null,
        vt_id: 131219,
        UBarCode: '',
        iPrtPerson: null
      };

      console.log('准备保存条码数据:', barcodeData);
      
      // 调用HYBarCodeMainController接口保存条码
      const response = await api.post('/api/inventory/hy-barcode-main', barcodeData);
      
      if (response.status === 201 || response.status === 200) {
        message.success(`条码生成成功，条码数量：${qty} ${record.unitName}，准备打印...`);
        
        // 更新本地数据 - 增加总打印次数
        setOrders(prevOrders => 
          prevOrders.map(order => 
            order.cPOID === record.cPOID && order.irowno === record.irowno
              ? { 
                  ...order, 
                  barcode: barcode, 
                  batchNumber: currentDate,
                  totalPrintCount: (order.totalPrintCount || 0) + 1
                }
              : order
          )
        );
        
        // 使用Ant Design QRCode组件的打印方案
        await performPrint(record, barcode, qty);
      }
      
    } catch (error) {
      console.error('条码生成失败:', error);
      message.error('条码生成失败: ' + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  // 改进的打印功能 - 使用Ant Design QRCode
  const performPrint = async (record, barcode, qty) => {
    try {
      // 1. 检查浏览器环境
      if (typeof window === 'undefined') {
        throw new Error('浏览器环境不可用');
      }

      // 2. 使用Ant Design QRCode生成二维码数据URL
      const qrCodeSize = 180;
      
      // 创建临时div用于渲染QRCode
      const tempDiv = document.createElement('div');
      tempDiv.style.position = 'absolute';
      tempDiv.style.left = '-9999px';
      document.body.appendChild(tempDiv);
      
      // 使用React方式创建QRCode
      const { createRoot } = await import('react-dom/client');
      const root = createRoot(tempDiv);
      
      return new Promise((resolve, reject) => {
        try {
          root.render(
            <QRCode
              value={barcode}
              size={qrCodeSize}
              errorLevel="M"
              color="#000000"
              bgColor="#ffffff"
              style={{ margin: 'auto' }}
            />
          );
          
          setTimeout(() => {
            try {
              const canvas = tempDiv.querySelector('canvas');
              if (canvas) {
                const qrCodeDataUrl = canvas.toDataURL('image/png');
                root.unmount();
                document.body.removeChild(tempDiv);
                
                // 使用生成的二维码图像创建打印内容
                const printContent = generatePrintHTMLWithQR(record, barcode, qty, qrCodeDataUrl);
                
                // 检查弹窗权限
                const popupTest = window.open('', '_blank', 'width=1,height=1,left=-1000,top=-1000');
                if (!popupTest || popupTest.closed) {
                  console.log('弹窗被阻止，使用备用方案');
                  iframePrintWithQR(record, barcode, qty, qrCodeDataUrl);
                  resolve();
                  return;
                }
                popupTest.close();

                // 创建打印窗口
                const printWindow = window.open('', '_blank', 'width=900,height=700,scrollbars=yes,resizable=yes');
                if (!printWindow) {
                  throw new Error('无法创建打印窗口');
                }

                // 使用现代DOM方法设置内容
                printWindow.document.open();
                printWindow.document.write(printContent);
                printWindow.document.close();
                printWindow.focus();

                setTimeout(() => {
                  if (printWindow && !printWindow.closed) {
                    printWindow.print();
                    setTimeout(() => {
                      if (printWindow && !printWindow.closed && window.confirm('打印完成，是否关闭窗口？')) {
                        printWindow.close();
                      }
                    }, 1000);
                  }
                  resolve();
                }, 500);
              } else {
                throw new Error('无法生成二维码');
              }
            } catch (error) {
              root.unmount();
              document.body.removeChild(tempDiv);
              reject(error);
            }
          }, 100);
        } catch (error) {
          reject(error);
        }
      });

    } catch (error) {
      console.error('打印错误:', error);
      message.warning('检测到浏览器限制，使用备用打印方案...');
      
      // 备用方案：使用简化二维码
      const printContent = generatePrintHTML(record, barcode, qty);
      const printWindow = window.open('', '_blank', 'width=900,height=700');
      printWindow.document.open();
      printWindow.document.write(printContent);
      printWindow.document.close();
      printWindow.print();
    }
  };

  // 使用iframe的备用打印方案 - 带二维码
  const iframePrintWithQR = async (record, barcode, qty, qrCodeDataUrl) => {
    try {
      const printContent = generatePrintHTMLWithQR(record, barcode, qty, qrCodeDataUrl);
      
      const iframe = document.createElement('iframe');
      iframe.style.position = 'fixed';
      iframe.style.top = '-10000px';
      iframe.style.left = '-10000px';
      iframe.style.width = '1px';
      iframe.style.height = '1px';
      
      document.body.appendChild(iframe);
      
      // 使用现代DOM方法设置内容
      iframe.contentDocument.open();
      iframe.contentDocument.write(printContent);
      iframe.contentDocument.close();
      
      // 触发打印
      setTimeout(() => {
        if (iframe.contentWindow) {
          iframe.contentWindow.focus();
          iframe.contentWindow.print();
          
          // 清理iframe
          setTimeout(() => {
            if (iframe.parentNode) {
              document.body.removeChild(iframe);
            }
          }, 2000);
        }
      }, 100);

    } catch (error) {
      console.error('iframe打印失败:', error);
      message.error('打印失败，请检查浏览器设置');
    }
  };

  // 生成带二维码的打印HTML - 调整为10cm x 7cm规格
  const generatePrintHTMLWithQR = (record, barcode, qty, qrCodeDataUrl) => {
    return `
      <!DOCTYPE html>
      <html>
        <head>
          <meta charset="UTF-8">
          <title>条码打印 - ${barcode}</title>
          <style>
            @media print {
              body { margin: 0; padding: 0; font-size: 10px; }
              .no-print { display: none !important; }
              .print-container { 
                page-break-inside: avoid;
                margin: 0;
                width: 70mm;
                height: 100mm;
                box-sizing: border-box;
              }
            }
            
            body { 
              font-family: 'Microsoft YaHei', Arial, sans-serif; 
              margin: 0;
              padding: 0;
              background: #fff;
              line-height: 1.2;
            }
            
            .print-container {
              background: white;
              border: 1px solid #333;
              border-radius: 4px;
              padding: 8px;
              margin: 0;
              width: 70mm;
              height: 100mm;
              box-sizing: border-box;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
            }
            
            .qr-code-container {
              text-align: center;
              margin: 4px 0;
            }
            
            .qr-code {
              display: inline-block;
              padding: 4px;
              background: #fff;
              border: 1px solid #333;
              border-radius: 2px;
            }
            
            .qr-code img {
              display: block;
              width: 45mm;
              height: 45mm;
            }
            
            .barcode-text {
              font-size: 11px;
              font-weight: bold;
              color: #000;
              letter-spacing: 0.5px;
              margin-top: 4px;
              padding: 2px;
              background: #f9f9f9;
              border: 1px solid #ccc;
              border-radius: 1px;
              word-break: break-all;
              text-align: center;
            }
            
            .item-info { 
              margin: 2px 0; 
              font-size: 9px;
              line-height: 1.1;
            }
            
            .item-info strong {
              color: #333;
              display: inline-block;
              width: 60px;
              font-weight: bold;
            }
            
            .print-header {
              text-align: center;
              margin-bottom: 4px;
              padding-bottom: 2px;
              border-bottom: 1px solid #333;
            }
            
            .print-header h2 {
              margin: 0;
              font-size: 12px;
              color: #333;
              font-weight: bold;
            }
            
            .print-footer {
              text-align: center;
              margin-top: 4px;
              padding-top: 2px;
              border-top: 1px solid #ccc;
              font-size: 8px;
              color: #666;
            }
            
            .no-print {
              margin: 10px 0;
              text-align: center;
            }
            
            .print-button { 
              background: #1890ff;
              color: white;
              border: none;
              padding: 4px 8px;
              border-radius: 2px;
              cursor: pointer;
              margin: 0 2px;
              font-size: 10px;
            }
            
            .print-button:hover { 
              background: #40a9ff;
            }
          </style>
        </head>
        <body>
          <div class="print-container">
            <div class="print-header">
              <h2>物料条码标签</h2>
            </div>
            
            <div class="qr-code-container">
              <div class="qr-code">
                <img src="${qrCodeDataUrl}" alt="二维码" />
              </div>
              <div class="barcode-text">${barcode}</div>
            </div>
            
            <div class="item-info">
              <strong>品名:</strong> ${record.cItemName}
            </div>
            <div class="item-info">
              <strong>编码:</strong> ${record.cInvCode}
            </div>
            <div class="item-info">
              <strong>数量:</strong> ${qty} ${record.unitName}
            </div>
            <div class="item-info">
              <strong>订单:</strong> ${record.cPOID}
            </div>
            <div class="item-info">
              <strong>供应商:</strong> ${record.supplierName}
            </div>
            <div class="item-info">
              <strong>批号:</strong> ${record.batchNumber || new Date().toISOString().split('T')[0]}
            </div>
            <div class="item-info">
              <strong>时间:</strong> ${new Date().toLocaleString('zh-CN', { 
                year: 'numeric', 
                month: '2-digit', 
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
              })}
            </div>
            
            <div class="print-footer">
              <p>江西江特电机有限公司 · 物料管理系统</p>
            </div>
            
            <div class="no-print">
              <button class="print-button" onclick="window.print()">打印</button>
              <button class="print-button" onclick="window.close()">关闭</button>
            </div>
          </div>
        </body>
      </html>
    `;
  };

  // 生成简化的打印HTML（备用方案）- 使用更优雅的二维码实现
  const generatePrintHTML = (record, barcode, qty) => {
    // 使用Google Charts API生成二维码，样式更接近Ant Design
    const qrCodeDataUrl = `https://chart.googleapis.com/chart?cht=qr&chs=160x160&chld=M|0&choe=UTF-8&chl=${encodeURIComponent(barcode)}`;
    
    return `
      <!DOCTYPE html>
      <html>
        <head>
          <meta charset="UTF-8">
          <title>条码打印 - ${barcode}</title>
          <style>
            @media print {
              body { margin: 0; padding: 0; font-size: 11px; }
              .no-print { display: none !important; }
              .print-container { 
                page-break-inside: avoid;
                margin: 0;
                width: 70mm;
                height: 100mm;
                box-sizing: border-box;
              }
            }
            
            body { 
              font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif; 
              margin: 0;
              padding: 0;
              background: #fff;
              line-height: 1.4;
              color: rgba(0, 0, 0, 0.85);
            }
            
            .print-container {
              background: white;
              border: 1px solid #d9d9d9;
              border-radius: 6px;
              padding: 12px;
              margin: 0;
              width: 70mm;
              height: 100mm;
              box-sizing: border-box;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            }
            
            .qr-code-container {
              text-align: center;
              margin: 8px 0;
            }
            
            .qr-code {
              display: inline-block;
              padding: 8px;
              background: #fff;
              border: 1px solid #f0f0f0;
              border-radius: 4px;
              box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
            }
            
            .qr-code img {
              display: block;
              width: 45mm;
              height: 45mm;
              border-radius: 2px;
            }
            
            .barcode-text {
              font-size: 11px;
              font-weight: 600;
              color: #262626;
              letter-spacing: 0.5px;
              margin-top: 6px;
              padding: 4px 6px;
              background: #fafafa;
              border: 1px solid #e8e8e8;
              border-radius: 4px;
              word-break: break-all;
              text-align: center;
              font-family: 'Courier New', monospace;
            }
            
            .item-info { 
              margin: 4px 0; 
              font-size: 10px;
              line-height: 1.3;
            }
            
            .item-info strong {
              color: #595959;
              display: inline-block;
              width: 55px;
              font-weight: 600;
            }
            
            .print-header {
              text-align: center;
              margin-bottom: 8px;
              padding-bottom: 4px;
              border-bottom: 1px solid #f0f0f0;
            }
            
            .print-header h2 {
              margin: 0;
              font-size: 13px;
              color: #262626;
              font-weight: 600;
            }
            
            .print-footer {
              text-align: center;
              margin-top: 8px;
              padding-top: 4px;
              border-top: 1px solid #f0f0f0;
              font-size: 9px;
              color: #8c8c8c;
            }
            
            .no-print {
              margin: 8px 0;
              text-align: center;
            }
            
            .print-button { 
              background: #1890ff;
              color: white;
              border: none;
              padding: 6px 12px;
              border-radius: 4px;
              cursor: pointer;
              margin: 0 3px;
              font-size: 10px;
              font-weight: 500;
              transition: all 0.3s ease;
            }
            
            .print-button:hover { 
              background: #40a9ff;
              box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
            }
          </style>
        </head>
        <body>
          <div class="print-container">
            <div class="print-header">
              <h2>物料条码标签</h2>
            </div>
            
            <div class="qr-code-container">
              <div class="qr-code">
                <img src="${qrCodeDataUrl}" alt="二维码" />
              </div>
              <div class="barcode-text">${barcode}</div>
            </div>
            
            <div class="item-info">
              <strong>品名:</strong> ${record.cItemName}
            </div>
            <div class="item-info">
              <strong>编码:</strong> ${record.cInvCode}
            </div>
            <div class="item-info">
              <strong>数量:</strong> ${qty} ${record.unitName}
            </div>
            <div class="item-info">
              <strong>订单:</strong> ${record.cPOID}
            </div>
            <div class="item-info">
              <strong>供应商:</strong> ${record.supplierName}
            </div>
            <div class="item-info">
              <strong>批号:</strong> ${record.batchNumber || new Date().toISOString().split('T')[0]}
            </div>
            <div class="item-info">
              <strong>时间:</strong> ${new Date().toLocaleString('zh-CN', { 
                year: 'numeric', 
                month: '2-digit', 
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
              })}
            </div>
            
            <div class="print-footer">
              <p>江西江特电机有限公司 · 物料管理系统</p>
            </div>
            
            <div class="no-print">
              <button class="print-button" onclick="window.print()">打印</button>
              <button class="print-button" onclick="window.close()">关闭</button>
            </div>
          </div>
        </body>
      </html>
    `;
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
      title: '采购员',
      dataIndex: 'personName',
      key: 'personName',
      sorter: (a, b) => (a.personName || '').localeCompare(b.personName || ''),
      width: 90
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
      width: 140
    },
    {
      title: '执行公司',
      dataIndex: 'cDefine1',
      key: 'cDefine1',
      sorter: (a, b) => a.cDefine1.localeCompare(b.cDefine1),
      width: 100
    },
    {
      title: '存货编码',
      dataIndex: 'cInvCode',
      key: 'cInvCode',
      sorter: (a, b) => a.cInvCode.localeCompare(b.cInvCode),
      width: 110
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
      width: 90
    },
    {
      title: '单位名称',
      dataIndex: 'unitName',
      key: 'unitName',
      sorter: (a, b) => a.unitName.localeCompare(b.unitName),
      width: 80
    },
    {
      title: '计划到货日期',
      dataIndex: 'dArriveDate',
      key: 'dArriveDate',
      render: (date) => date ? new Date(date).toLocaleDateString() : '',
      sorter: (a, b) => new Date(a.dArriveDate) - new Date(b.dArriveDate),
      width: 100
    },
    {
      title: '总打印次数',
      dataIndex: 'totalPrintCount',
      key: 'totalPrintCount',
      render: (count) => (
        <div style={{ textAlign: 'center', fontWeight: 'bold', color: count > 0 ? '#1890ff' : '#999' }}>
          {count || 0}
        </div>
      ),
      sorter: (a, b) => (a.totalPrintCount || 0) - (b.totalPrintCount || 0),
      width: 90
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
      width: 110
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
                      <Form.Item name="personName" label="采购员" className="search-item">
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
                onRow={() => ({
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
