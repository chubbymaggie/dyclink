package edu.columbia.psl.cc.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import edu.columbia.psl.cc.util.StringUtil;

public class InstNode implements Comparable<InstNode>{
	
	private Var var;
	
	private int idx;
	
	private OpcodeObj op;
	
	private String addInfo = "";
	
	private String fromMethod = "";
	
	private int linenumber;
	
	private int startTime = -1;
	
	private int updateTime = -1;
	
	private AtomicInteger maxSurrogate = new AtomicInteger();
	
	private ArrayList<String> dataParentList = new ArrayList<String>();
	
	private ArrayList<String> controlParentList = new ArrayList<String>();
	
	//For freq map, the key is the inst method + idx, and the value is the frequency
	private TreeMap<String, Double> childFreqMap = new TreeMap<String, Double>();
	
	//Key surrogate id, value child node rep
	private HashMap<String, Integer> surrogates = new HashMap<String, Integer>();
	
	private Object relatedObj = null;
	
	public InstNode() {
		
	}
	
	public InstNode(InstNode copy) {
		this.idx = copy.getIdx();
		this.startTime = copy.getStartTime();
		this.updateTime = copy.getUpdateTime();
		this.op = copy.getOp();
		this.addInfo = copy.getAddInfo();
		this.fromMethod = copy.getFromMethod();
		this.dataParentList = new ArrayList<String>(copy.getDataParentList());
		this.controlParentList = new ArrayList<String>(copy.getControlParentList());
		this.childFreqMap = new TreeMap<String, Double>(copy.getChildFreqMap());
	}
	
	public void registerParent(String fromMethod, int parentIdx, boolean isControl) {
		String idxKey = StringUtil.genIdxKey(fromMethod, parentIdx);
		if (!isControl && !this.dataParentList.contains(idxKey)) {
			this.dataParentList.add(idxKey);
		} else if (isControl && !this.controlParentList.contains(idxKey)){
			this.controlParentList.add(idxKey);
		}
	}
	
	public void setDataParentList(ArrayList<String> dataParentList) {
		this.dataParentList = dataParentList;
	}
	
	public ArrayList<String> getDataParentList() {
		return this.dataParentList;
	}
	
	public void setControlParentList(ArrayList<String> controlParentList) {
		this.controlParentList = controlParentList;
	}
	
	public ArrayList<String> getControlParentList() {
		return this.controlParentList;
	}
	
	public void increChild(String fromMethod, int childIdx, double amount) {
		String idxKey = StringUtil.genIdxKey(fromMethod, childIdx);
		if (this.childFreqMap.containsKey(idxKey)) {
			double count = this.childFreqMap.get(idxKey) + amount;
			this.childFreqMap.put(idxKey, count);
		} else {
			this.childFreqMap.put(idxKey, amount);
		}
	}
	
	public void setChildFreqMap(TreeMap<String, Double> childFreqMap) {
		this.childFreqMap = childFreqMap;
	}
	
	public TreeMap<String, Double> getChildFreqMap() {
		return this.childFreqMap;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public int getIdx() {
		return this.idx;
	}
	
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
	public int getStartTime() {
		return this.startTime;
	}
	
	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}
	
	public int getUpdateTime() {
		return this.updateTime;
	}
	
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	
	public String getAddInfo() {
		return this.addInfo;
	}
	
	public void setOp(OpcodeObj op) {
		this.op = op;
	}
	
	public OpcodeObj getOp() {
		return this.op;
	}
		
	public void setFromMethod(String fromMethod) {
		this.fromMethod = fromMethod;
	}
	
	public String getFromMethod() {
		return this.fromMethod;
	}
	
	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}
	
	public int getLinenumber() {
		return this.linenumber;
	}
	
	public void setRelatedObj(Object relatedObj) {
		this.relatedObj = relatedObj;
	}
	
	public Object getRelatedObj() {
		return this.relatedObj;
	}
	
	public void putSurrogate(String childNodeRep, int surrogateId) {
		this.surrogates.put(childNodeRep, surrogateId);
	}
	
	public HashMap<String, Integer> getSurrogates() {
		return this.surrogates;
	}
	
	public void setMaxSurrogate(int baseId) {
		this.maxSurrogate.set(baseId);
	}
	
	public int getMaxSurrogate() {
		return this.maxSurrogate.getAndIncrement();
	}
	
	public int probeSurrogate() {
		return this.maxSurrogate.get();
	}
			
	public void setVar(Var v) {
		this.var = v;
	}
	
	public Var getVar() {
		return this.var;
	}
	
	public boolean isLoad() {
		//Exclude aload series
		if (this.getOp().getCatId() == 1)
			return true;
		else 
			return false;
	}
	
	public boolean isArrayLoad() {
		if (this.getOp().getCatId() == 2)
			return true;
		else
			return false;
	}
	
	public boolean isStore() {
		if (this.getOp().getCatId() == 3)
			return true;
		else 
			return false;
	}
	
	public boolean isArrayStore() {
		if (this.getOp().getCatId() == 4)
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		return this.fromMethod + " " + this.idx + " " + this.op.getOpcode() + " " + this.op.getInstruction() + " " + this.getAddInfo();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof InstNode))
			return false;
		
		InstNode tmpNode = (InstNode)o;
		
		if (!tmpNode.toString().equals(this.toString()))
			return false;
		else
			return true;
	}

	@Override
	public int compareTo(InstNode other) {
		int methodCompare = this.getFromMethod().compareTo(other.getFromMethod());
		if (methodCompare != 0)
			return methodCompare;
		else
			return (this.getIdx() > other.getIdx())?1:((this.getIdx() < other.getIdx()))?-1:0;
	}

}
