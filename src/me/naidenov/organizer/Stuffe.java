package me.naidenov.organizer;

import java.util.Date;

public class Stuffe {
	private String Title;
	private String Type;
	private int Id;
	private Date Date;
	private int ListPossiton;

	public int getListPossiton() {
		return ListPossiton;
	}

	public void setListPossiton(int listPossiton) {
		ListPossiton = listPossiton;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getType() {
		return Type;
	}

	public Date getDate() {
		return Date;
	}

	public void setDate(Date date) {
		Date = date;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

}
