const app = getApp();
const util = require('../../utils/util.js');
var id = 1;
var logo="";
var title="";
var price=0;


Page({
  data: {
    posterConfig: {},
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
  },
  onLoad: function (options) {
    this.aposter = this.selectComponent('#aposterid');
    var that = this;
    id = options.id;
    id=1;
    that.getDetails(id);

  },
  wxmlTagATap(e) {
    console.log(e);
  },
  onPullDownRefresh() {
    this.onLoad();
  },
  onPosterSuccess(e) {
    const { detail } = e;
    wx.previewImage({
      current: detail,
      urls: [detail]
    })
  },
  onPosterFail(err) {
    console.error(err);
  },

 /**
   * 异步生成海报
   */
  onCreateOtherPoster() {
    this.aposter.genPoster({
      autoHeight: true,
      productImg: 'http://img14.360buyimg.com/n1/jfs/t11389/290/2985708817/209753/ef3ac542/5cdcc7afNa56599fc.jpg', // short image
      title: '标题',
      price: '￥99',
      avatar: '../avatar.jpg',
      nickname: 'Feynman',
      motto: '推荐你使用acanvas，享编程乐趣',
      qrCode: 'https://mini.zyzygame.com/weapp/share?id=1',
      genQrCode: true,
      showLoading: true
    });
  },   
   onShareAppMessage() {
     return {
       path: '/pages/details/details?id=' + id
     }
  },
  showModal(e) {
    //二维码弹窗
    this.setData({
      modalName: e.currentTarget.dataset.target
    })
  }, 
  hideModal(e) {
    this.setData({
      modalName: null
    })
  }, 
  getDetails: function (id) {
    wx.request({
      url: util.basePath+'pro/details?id=' + id,
      success: res => {
        console.log(res);
        if (res.statusCode == 200 && res.data.code == 0) {
            wx.stopPullDownRefresh();

          this.logo = util.imgViewPath + res.data.product.logo;
          this.title = res.data.product.title;
          this.price = res.data.product.newprice

            this.setData({
              title: res.data.product.title,
              newprice: res.data.product.newprice,
              oldprice: res.data.product.oldprice,
              details: res.data.product.details,
              logo: res.data.product.logo
          });


        } else {
            wx.showToast({ title: '数据异常', icon: "none" })
        }
      }
    })
  },
  
/**
* 页面相关事件处理函数--监听用户下拉动作
*/
  onPullDownRefresh: function () {
    // // 显示顶部刷新图标
    // wx.showNavigationBarLoading();
    this.onLoad();
    setTimeout(function () {
      // 隐藏导航栏加载框
      // wx.hideNavigationBarLoading();
      //停止当前页面下拉刷新。
      wx.stopPullDownRefresh()
    }, 1500)

  },
  showQrcode:function(){
    wx.previewImage({
      urls: ['http://lovevivian.com/images/wechatcode.jpg'],
      current: 'http://lovevivian.com/images/wechatcode.jpg' // 当前显示图片的http链接      
    })
  }
})


