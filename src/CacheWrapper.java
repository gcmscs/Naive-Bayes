import java.io.Serializable;
import java.util.Map;


public class CacheWrapper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, DocInfo> dataModel;
	private double[] column;
	private double dataModelOneCount;
	private double dataModelZeroCount;
	
	public double getDataModelOneCount() {
		return dataModelOneCount;
	}
	public void setDataModelOneCount(double dataModelOneCount) {
		this.dataModelOneCount = dataModelOneCount;
	}
	public double getDataModelZeroCount() {
		return dataModelZeroCount;
	}
	public void setDataModelZeroCount(double dataModelZeroCount) {
		this.dataModelZeroCount = dataModelZeroCount;
	}
	public CacheWrapper(Map<String, DocInfo> dataModel, double[] column){
		this.dataModel = dataModel;
		this.column = column;
	}
	public Map<String, DocInfo> getDataModel() {
		return dataModel;
	}
	public void setDataModel(Map<String, DocInfo> dataModel) {
		this.dataModel = dataModel;
	}
	public double[] getColumn() {
		return column;
	}
	public void setColumn(double[] column) {
		this.column = column;
	}

}
