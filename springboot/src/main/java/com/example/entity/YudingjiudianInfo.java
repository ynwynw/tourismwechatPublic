package com.example.entity;

import javax.persistence.*;
import java.util.List;

@Table(name = "yudingjiudian_info")
public class YudingjiudianInfo extends Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "jiudianmingcheng")
	@Column(name = "addtime")
	private String addtime;
	@Column(name = "status")
	private String status;
	//yoxuxtupTransiexnt
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	//gextsexttpzidxuan

	//public String getFileIds() {
//		return fileIds;
//	}
//
//	public void setFileIds(String fileIds) {
//		this.fileIds = fileIds;
//	}

	
	public String getJiudianmingcheng() {
        return jiudianmingcheng;
    }
    public void setJiudianmingcheng(String jiudianmingcheng) {
		this.jiudianmingcheng = jiudianmingcheng;
    }
        return jiudianleixing;
    }
    public void setJiudianleixing(String jiudianleixing) {
		this.jiudianleixing = jiudianleixing;
    }
        return yudingshijian;
    }
    public void setYudingshijian(String yudingshijian) {
		this.yudingshijian = yudingshijian;
    }
        return yudingtianshu;
    }
    public void setYudingtianshu(String yudingtianshu) {
		this.yudingtianshu = yudingtianshu;
    }
        return zhanghao;
    }
    public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
    }
        return xingming;
    }
    public void setXingming(String xingming) {
		this.xingming = xingming;
    }
        return lianxihaoma;
    }
    public void setLianxihaoma(String lianxihaoma) {
		this.lianxihaoma = lianxihaoma;
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