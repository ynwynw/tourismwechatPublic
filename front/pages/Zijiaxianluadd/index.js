﻿import {request} from "../../request/index.js";
import { formatTime, formatDate,addTime } from '../../utils/util.js';
import {config} from '../../request/config'
const app = getApp()
const appG = app.globalData;

Page({
  data: {
  		xianlumingcheng: '',		chufadi: '',		mudedi: '',		quanchengjuli: '',		xiangqingmiaoshu: '',		source: '/imgs/addimg.png',		tupianx:'',		
		hsgid:0,
  },
  
  onShow: function (options) {
  	//xiabiaoduan1
    let user = wx.getStorageSync('user');
    if (!user) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      }) 
      wx.navigateTo({
        url: '/pages/login/index'
      });
    } else {
    }
    this.setData({
      user,
	  
    })
  },
  
   onLoad: function (options) {
    let id = options.id
    let user = wx.getStorageSync('user');
    if (!user) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      }) 
      wx.navigateTo({
        url: '/pages/login/index'
      });
    } else {
     	this.setData({
        hsgid: id
      })
    }
    var that = this;
    var TIME = formatTime(new Date());
    this.setData({
      user,
      
    })
	
	
   
  },
  
  
  
  /**
   * 上传图片
   */
  uploadimg: function () {
    var that = this;
    wx.chooseImage({ //从本地相册选择图片或使用相机拍照
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        //console.log(res)
        //前台显示
        that.setData({
          source: res.tempFilePaths
        })
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url:'http://localhost:8888/files/upload',
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type': 'multipart/form-data'
          },
          formData: {
            method: 'POST'   //请求方式
          },
          success: function (res) {
            var queryBean = JSON.parse(res.data);
            var fileurl = queryBean.data.id;
			var filename = queryBean.data.originName;
            that.setData({
              tupianx: config.baseFileUrl+fileurl,
              tupian: JSON.stringify([fileurl]),
              status:'上传成功'
             });
            }
        })
      }
    })
  },
  
   // 获取
   xianlumingchengInput: function (e) {
    this.setData({
      xianlumingcheng: e.detail.value,
    })
  },		chufadiInput: function (e) {
    this.setData({
      chufadi: e.detail.value,
    })
  },		mudediInput: function (e) {
    this.setData({
      mudedi: e.detail.value,
    })
  },		quanchengjuliInput: function (e) {
    this.setData({
      quanchengjuli: e.detail.value,
    })
  },						
  // add
  login: function () {
    let user = wx.getStorageSync('user');
    var params = {
		xianlumingcheng:this.data.xianlumingcheng,chufadi:this.data.chufadi,mudedi:this.data.mudedi,quanchengjuli:this.data.quanchengjuli,xiangqingmiaoshu:this.data.xiangqingmiaoshu,tupian:this.data.tupian,
    }
	
	
    request({
      url: '/zijiaxianluInfo',
      method: 'POST',
      data: params,
      header: {
        'content-type': 'application/json' 
      }}).then(res => {
      if(res.code === "0") {
        wx.showToast({
          title: '提交成功',
          icon: 'none',
        })
       // wx.switchTab({
       //   url: '/pages/Zijiaxianlulist/index',
       //  })
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none'
        })
      }
    })
	
	
  },

 
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  

})