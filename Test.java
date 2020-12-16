package com.chitwan.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.springframework.util.StringUtils;

/**
 * The given string has pipe delimited nodes that represent family members in a family tree. 
 *		Each node is a CSV with the values being "parent_id, node_id, node_name". 
 *		Write a method that takes an input string and return a single result that 
 *		represents the data as a hierarchy (root, children, siblings, etc).
 *		Sample input: 
 *			null,0,grandpa|0,1,son|0,2,daugther|1,3,grandkid|1,4,grandkid|2,5,grandkid|5,6,greatgrandkid
 */

public class Test {
	
	public static void main(String[] args) {
		
		String family = null;
		
		try (Scanner scanner= new Scanner(System.in)){
			System.out.print("Enter the family string in pipe delimitted: ");
			family = scanner.nextLine();
		}
		if (!StringUtils.hasText(family)) {
			//No CSV provided
			System.out.print("No CSV command line provided ");
			return;
		}
		String[] lines =  family.split("\\|");
		List<Node> linkedList = new LinkedList<>();
		if (lines != null) {
			for (String line: lines) {
				String[] details = line.split(",");
				if (details == null || details.length != 3) {
					//Data not in correct format so ignore the line
					continue;
				}
				Node node = new Node();
				node.setId(Integer.parseInt(details[1].trim()));
				node.setName(details[2].trim());
				Node parentNode = 
					linkedList.stream().filter(p -> p.getId().equals(Integer.parseInt(details[0].trim()))).findAny().orElse(null);
				if (parentNode != null) {
					node.setParentId(parentNode);
					if (parentNode.getChildIds() == null) {
						parentNode.setChildIds(new ArrayList<>());
					}
					parentNode.getChildIds().add(node);	
				}
				linkedList.add(node);
			}
		}
		Node rootNode = linkedList.stream().filter(n-> n.getParentId() == null).findFirst().orElse(null);
		if (rootNode != null) {
			StringBuilder sb = new StringBuilder();
			buildHierarchy(sb, rootNode, 0);
		    System.out.println(sb.toString());
		}
			
	}
	
	private static void buildHierarchy(StringBuilder sb, Node node, int level) {
	    if (node != null) {
	        sb.append(node);
	        sb.append("\n");
	       
	        if (node.getChildIds() != null) {
	        	level++;
	        	for (Node n:node.getChildIds()) {
	        		sb.append(printLevels(level));
	        		buildHierarchy(sb, n, level);
	    		}
	        } 
	        
	    }
	}
	
	private static StringBuilder printLevels(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i =0; i<level; i++) {
			sb.append("\t");
		}
		return sb;
		
	}


}



class Node {
	private Integer id;
	private String name;
	private Node parentId;
	private List<Node> childIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Node getParentId() {
		return parentId;
	}
	public void setParentId(Node parentId) {
		this.parentId = parentId;
	}
	public List<Node> getChildIds() {
		return childIds;
	}
	public void setChildIds(List<Node> childIds) {
		this.childIds = childIds;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
        sb.append("(").append(this.getId()).append(")");
        sb.append("\n");
    	return sb.toString();
	}
	
	
}
