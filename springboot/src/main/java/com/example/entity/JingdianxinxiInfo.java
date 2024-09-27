package com.example.entity;

import javax.persistence.*;
import java.util.List;

@Table(name = "jingdianxinxi_info")
public class JingdianxinxiInfo extends Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "jingdianmingcheng")
	@Column(name = "addtime")
	private String addtime;
	@Column(name = "status")
	private String status;
	@Transient private List<Long> tupianflst;	//yoxuxtupTransiexnt
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<Long>  getTupianflst() {
        return tupianflst;
    }
    public void setTupianflst(List<Long> tupianflst) {
		this.tupianflst = tupianflst;
    }

	//public String getFileIds() {
//		return fileIds;
//	}
//
//	public void setFileIds(String fileIds) {
//		this.fileIds = fileIds;
//	}

	
	public String getJingdianmingcheng() {
        return jingdianmingcheng;
    }
    public void setJingdianmingcheng(String jingdianmingcheng) {
		this.jingdianmingcheng = jingdianmingcheng;
    }
        return jibie;
    }
    public void setJibie(String jibie) {
		this.jibie = jibie;
    }
        return danjiwangji;
    }
    public void setDanjiwangji(String danjiwangji) {
		this.danjiwangji = danjiwangji;
    }
        return yingyeshijian;
    }
    public void setYingyeshijian(String yingyeshijian) {
		this.yingyeshijian = yingyeshijian;
    }
        return piaojia;
    }
    public void setPiaojia(String piaojia) {
		this.piaojia = piaojia;
    }
        return weizhi;
    }
    public void setWeizhi(String weizhi) {
		this.weizhi = weizhi;
    }
        return tupian;
    }
    public void setTupian(String tupian) {
		this.tupian = tupian;
    }
        return jianjie;
    }
    public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
    }
        return dianzan_d;
    }
    public void setDianzan_d(String dianzan_d) {
		this.dianzan_d = dianzan_d;
    }
        return dianzan_c;
    }
    public void setDianzan_c(String dianzan_c) {
		this.dianzan_c = dianzan_c;
    }

	public String getAddtime() {
        return addtime;
    }
    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return this.id;
	}

}