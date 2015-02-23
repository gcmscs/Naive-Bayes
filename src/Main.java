import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Main {
	
	private static double dataModelOneCount = 0.0;
	private static double dataModelZeroCount = 0.0;
	private static double numberOfAttributes = 0.0;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		/*String fileName = "train - Copy.csv";
		String fileName1 = "train_labels - Copy.txt";
		String fileName2 = "test - Copy.csv";
		String fileName3 = "test_labels - Copy.txt";*/
		String fileName = "train.csv";
		String fileName1 = "train_labels.txt";
		String fileName2 = "test.csv";
		String fileName3 = "test_labels.txt";
		buildDataModel(fileName, fileName1, fileName2, fileName3);
	}
	
	private static boolean isCache(){
		if(new File("dataModelOne.ser").exists() && new File("dataModelZero.ser").exists())
			return true;
		else 
			return false;
	}
	
	private static void buildDataModel(String trainingFile, String trainingLabel, String testFile, String testLabel) throws IOException, ClassNotFoundException{
		int docId = 0;
		String line = "";
		DocInfo correspDocObj = null;
		Map<String, DocInfo> dataModelMap;
		Map<String, DocInfo> dataModelMapOne;
		Map<String, DocInfo> dataModelMapZero;
		BufferedReader br;
		double[] columnSum0;
		double[] columnSum1;
		String key;
		if (!isCache()) {
			dataModelMap = new HashMap<String, DocInfo>();
			dataModelMapOne = new HashMap<String, DocInfo>();
			dataModelMapZero = new HashMap<String, DocInfo>();
			br = new BufferedReader(new FileReader(new File(trainingFile)));
			int n = 0;
			while ((line = br.readLine()) != null) {
				n = line.split(",").length;
				if (n > 0)
					break;
			}
			columnSum0 = new double[n];
			columnSum1 = new double[n];
			br = new BufferedReader(new FileReader(new File(trainingFile)));
			while ((line = br.readLine()) != null) {
				docId++;
				correspDocObj = new DocInfo();
				correspDocObj.setData(line);
				dataModelMap.put("docId" + docId, correspDocObj);
			}
			docId = 0;
			line = null;
			key = null;
			String line1 = null;
			String line2 = null;
			correspDocObj = null;
			br = new BufferedReader(new FileReader(new File(trainingLabel)));
			int count = 0;
			while ((line = br.readLine()) != null) {
				docId++;
				key = "docId" + docId;
				correspDocObj = dataModelMap.get(key);
				correspDocObj.setLabel(line);
				if (line.equals("1")) {
					dataModelMapOne.put(key, correspDocObj);
					line1 = correspDocObj.getData();
					dataModelOneCount += parse(line1);
					for (int i = 0; i < n; i++) {
						columnSum1[i] += Double
								.parseDouble(line1.split(",")[i]);
					}
					correspDocObj.setColumn(columnSum1);
				} else if (line.equals("0")) {
					dataModelMapZero.put(key, correspDocObj);
					line2 = correspDocObj.getData();
					dataModelZeroCount += parse(line2);
					for (int i = 0; i < n; i++) {
						columnSum0[i] += Double
								.parseDouble(line2.split(",")[i]);
					}
					correspDocObj.setColumn(columnSum0);
				}
				count++;
				System.out.println("Count: " + count);
			}
			prepareCache(dataModelMapOne, dataModelMapZero, columnSum0, columnSum1);
		}
		else{
			FileInputStream fos = new FileInputStream(new File("dataModelOne.ser"));
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fos));
			CacheWrapper cw = ((CacheWrapper)in.readObject());
			dataModelMapOne = cw.getDataModel();
			columnSum1 = cw.getColumn();
			dataModelOneCount = cw.getDataModelOneCount();
			in.close();
			fos.close();
			fos = new FileInputStream(new File("dataModelZero.ser"));
			in = new ObjectInputStream(new BufferedInputStream(fos));
			cw = ((CacheWrapper)in.readObject());
			dataModelMapZero = cw.getDataModel();
			columnSum0 = cw.getColumn();
			dataModelZeroCount = cw.getDataModelZeroCount();
			in.close();
			fos.close();
		}
		System.out.println("Data model prepared...");
		numberOfAttributes = dataModelMapOne.get("docId"+1).getData().split(",").length;
		System.out.println("All variables set...");
		System.out.println("Starting to traverse test data...");
		docId = 0;
		line = null;
		correspDocObj = null;
		Map<String, DocInfo> testDataModelMap = new HashMap<String, DocInfo>();
		br = new BufferedReader(new FileReader(new File(testFile)));
		while((line = br.readLine())!=null){
			docId++;
			correspDocObj = new DocInfo();
			correspDocObj.setData(line);
			testDataModelMap.put("docId"+docId, correspDocObj);
		}
		docId = 0;
		key = null;
		correspDocObj = null;
		br = new BufferedReader(new FileReader(new File(testLabel)));
		while((line = br.readLine())!=null){
			docId++;
			key = "docId"+docId;
			correspDocObj = testDataModelMap.get(key);
			correspDocObj.setLabel(line);
		}
		int county = 0;
		Iterator<Entry<String, DocInfo>> iterator = testDataModelMap.entrySet().iterator();
		while(iterator.hasNext()){
			county++;
			Entry<String, DocInfo> entry = iterator.next();
			DocInfo valueItr = entry.getValue();
			double s0 = dataModelMapZero.size();
			double s1 = dataModelMapOne.size();
			double p0 = s0/(s1+s0);
			double p1 = s1/(s1+s0);
			int count1 = 0;
			for (int i = 0; i < numberOfAttributes; i++) 
			{
				count1++;
				double power = new Double(valueItr.getData().split(",")[i]).intValue();
				p0 = p0*Math.pow(probability(columnSum0[i], dataModelZeroCount), power);
				p1 = p1*Math.pow(probability(columnSum1[i], dataModelOneCount), power);
				//System.out.println(count1+"-th iteration completed...");
			}
			//probabilities prepared...
			//comparison starts: preference given to p1
			if(p1>=p0)
			{
				valueItr.setOutputLabel("1");
			}
			else
			{
				valueItr.setOutputLabel("0");
			}
			System.out.println("county: "+county);
		}
		double accuracy = 0.0;
		double size = testDataModelMap.size();
		iterator = testDataModelMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, DocInfo> entry = iterator.next();
			String label = entry.getValue().getLabel();
			String labelo = entry.getValue().getOutputLabel();
			if(label.equals(labelo)){
				accuracy++;
			}
		}
		accuracy = accuracy/size*100.0;
		System.out.println("Accuracy... "+accuracy+" %");
	}
	
	private static void prepareCache(Map<String, DocInfo> dataModelMapOne, Map<String, DocInfo> dataModelMapZero, double[] columns0, double[] columns1) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(new File("dataModelOne.ser"));
		ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(fos));
		CacheWrapper cw = new CacheWrapper(dataModelMapOne, columns1);
		cw.setDataModelOneCount(dataModelOneCount);
		os.writeObject(cw);
		os.close();
		fos.close();
		fos = new FileOutputStream(new File("dataModelZero.ser"));
		os = new ObjectOutputStream(new BufferedOutputStream(fos));
		cw = new CacheWrapper(dataModelMapZero, columns0);
		cw.setDataModelZeroCount(dataModelZeroCount);
		os.writeObject(cw);
		os.close();
		fos.close();
	}

	private static double probability(double columnSum, double dataModelCount){
		double p = (columnSum+1)/(dataModelCount+numberOfAttributes);
		return p;
	}
	
	private static double parse(String line1){
		double curr = 0.0;
		String[] lineArr = line1.trim().split(",");
		for(int i=0;i<lineArr.length;i++){
			curr += Double.parseDouble(lineArr[i]);
		}
		return curr;
	}

}
