package com.flchy.blog.privilege.config.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.enums.MenuTypeEnum;

public class BaseMenu extends BaseInfoMenuEntity {
    private static final long serialVersionUID = 5795705904587295970L;
    // 子菜单集合
    private List<BaseMenu> children;
    private String label;
    private String selected;
    

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BaseMenu() {
    }

    public BaseMenu(int menuId) {
        this.setMenuId(menuId);
    }

    public List<BaseMenu> getChildren() {
        return children;
    }

    public void setChildren(List<BaseMenu> children) {
        this.children = children;
    }

    public void addChild(BaseMenu menuBean) {
        if (this.children == null) {
            this.children = new ArrayList<BaseMenu>();
        }
        this.children.add(menuBean);
    }

    public void addChildren(List<BaseMenu> menuList) {
        if (this.children == null) {
            this.children = new ArrayList<BaseMenu>();
        }
        this.children.addAll(menuList);
    }

    public void clearChildren() {
        if (this.children != null) {
            this.children.clear();
        }
    }

    // 判断是否是目录
    public boolean isFolder() {
        return MenuTypeEnum.FOLDER.getId() == this.getMenuType().intValue();
    }

    // 判断是否是菜单项
    public boolean isMenuItem() {
        return MenuTypeEnum.MENU.getId() == this.getMenuType().intValue();
    }

    // 是否含有菜单项,用于去掉空的目录项
    public boolean isEmptyFolder() {
        boolean isEmpty = true;
        // 如果是目录或主题,递归判断是否有子菜单
        if (this.getMenuType().intValue() == MenuTypeEnum.FOLDER.getId()) {
            if (CollectionUtils.isEmpty(this.getChildren())) {
                isEmpty = true;
            } else {
                for (BaseMenu menuBean : this.getChildren()) {
                    if (!menuBean.isEmptyFolder()) {
                        isEmpty = false;
                        break;
                    }
                }
            }
        } else {
            isEmpty = false;
        }
        return isEmpty;
    }

    public void sort() {
        if (CollectionUtils.isEmpty(this.children)) {
            return;
        }
        Collections.sort(this.children, new Comparator<BaseMenu>() {
            public int compare(BaseMenu menuBean1, BaseMenu menuBean2) {
                return menuBean1.getSortIndex().compareTo(menuBean2.getSortIndex());
            }
        });
        for (BaseMenu menuBean : this.children) {
            menuBean.sort();
        }
    }
}
