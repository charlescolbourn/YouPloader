package at.becast.youploader.templates;

public class Item {

    private int id;
    private Template template;

    public Item(int id, Template template) {
        this.id = id;
        this.template = template;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String toString(){
        return this.template.name;
    }
}