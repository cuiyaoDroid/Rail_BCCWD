package com.xzh.rail.bccwd.entry;

import java.io.Serializable;
import java.util.List;

import android.Manifest.permission;


public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String loginName;
	private String password;
	private String name;
	private String spellName;
	private String lastLoginTime;
	private int loginCount;
	private String token;
	private int departmentId;
	private boolean deleted;
	private int stationId;
	private int userLevelId;
	private boolean departmentHead;
	private List<Positions> positions;
	private List<Permissions> permissions;
	private Department department;
	private Station station;
	private UserLevel userLevel;
	private String photoLabel;
	private String position;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpellName() {
		return spellName;
	}
	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public int getUserLevelId() {
		return userLevelId;
	}
	public void setUserLevelId(int userLevelId) {
		this.userLevelId = userLevelId;
	}
	public boolean isDepartmentHead() {
		return departmentHead;
	}
	public void setDepartmentHead(boolean departmentHead) {
		this.departmentHead = departmentHead;
	}
	public List<Positions> getPositions() {
		return positions;
	}
	public void setPositions(List<Positions> positions) {
		this.positions = positions;
	}
	public List<Permissions> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permissions> permissions) {
		this.permissions = permissions;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	public UserLevel getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}
	public String getPhotoLabel() {
		return photoLabel;
	}
	public void setPhotoLabel(String photoLabel) {
		this.photoLabel = photoLabel;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
