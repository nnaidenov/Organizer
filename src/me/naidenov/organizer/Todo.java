package me.naidenov.organizer;

public class Todo {
	private String Title;
	private String Description;
	private int Id;
	private Boolean IsDone;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Boolean getIsDone() {
		return IsDone;
	}

	public void setIsDone(Boolean isDone) {
		IsDone = isDone;
	}
}
