import java.io.Serializable;


public class DocInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private String data;
	private String outputLabel;
	private double[] column;
	
	public double[] getColumn() {
		return column;
	}
	public void setColumn(double[] column) {
		this.column = column;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getOutputLabel(){
		return outputLabel;
	}
	public void setOutputLabel(String outputLabel){
		this.outputLabel = outputLabel;
	}

}
