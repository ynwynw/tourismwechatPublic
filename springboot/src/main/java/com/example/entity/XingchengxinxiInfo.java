package com.example.entity;

import javax.persistence.*;
import java.util.List;

@Table(name = "xingchengxinxi_info")
public class XingchengxinxiInfo extends Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "xingchengriqi")
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

	
	public String getXingchengriqi() {
        return xingchengriqi;
    }
    public void setXingchengriqi(String xingchengriqi) {
		this.xingchengriqi = xingchengriqi;
    }
        return chufadi;
    }
    public void setChufadi(String chufadi) {
		this.chufadi = chufadi;
    }
        return mudedi;
    }
    public void setMudedi(String mudedi) {
		this.mudedi = mudedi;
    }
        return beizhu;
    }
    public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
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