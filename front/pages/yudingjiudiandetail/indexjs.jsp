import {request} from '../../request/index.js'
import {config} from '../../request/config'

Page({

  /**
   * ҳ��ĳ�ʼ����
   */
  data: {
    obj: {},
    goodsId: 0,
    commentList: []
  },
  
  

  /**
   * �������ں���--����ҳ�����
   */
  onLoad: function (options) {
    let user = wx.getStorageSync('user');
    if (!user) {
      wx.showToast({
        title: '���ȵ�¼',
        icon: 'none'
      }) 
      wx.navigateTo({
        url: '/pages/login/index'
      });
    } else {
    }
    const id = options.id;
    this.setData({
      userTextId: id
    })
    // ��ȡ����
    this.getDetail(id);
  },
  //jixaaxnnxiu
  getDetail(id) {
    request({url: '/yudingjiudianInfo/' + id}).then(res => {
      if(res.code === '0') {
        let obj = res.data;
			
        
        this.setData({
          obj
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none'
        })
      }
    })
  },
  //ddxianzxan
})
