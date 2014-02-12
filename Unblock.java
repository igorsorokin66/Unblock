package Unblock;

import java.util.ArrayList;

public class Unblock
{
	static String grid[][] = new String[7][7];
	static class Block
	{
		Block(String p1, int x1, int y1, int l1)
		{
			p = p1; x = x1; y = y1; l = l1;
		}
		String p = "";
		int x = 0;
		int y = 0;
		int l = 0;
		
		boolean downFlag = false;
	}
	
	static ArrayList<Block> blocks = new ArrayList<Block>();
	static Block red;
	public static void main(String[] args)
	{
		String a[] = {"hori 1 1 3", 
					  "hori 3 1 2 r", 
					  "vert 1 6 3",
					  "vert 2 3 3",
					  "hori 4 5 2",
					  "vert 4 1 2",
					  "hori 6 1 3",
					  "vert 5 5 2"};
		String cc[] = {"#","@","&","$","*","+","=","%"};
		int c = 0;
		for (String x : a)
		{
			blocks.add(new Block(x.split(" ")[0], Integer.valueOf(x.split(" ")[1]), Integer.valueOf(x.split(" ")[2]), Integer.valueOf(x.split(" ")[3])));
			if (x.charAt(x.length()-1) == 'r') red = blocks.get(blocks.size()-1);
			if (x.split(" ")[0].equals("hori"))
			{
				for (int i = Integer.valueOf(x.split(" ")[2]); i < Integer.valueOf(x.split(" ")[2])+Integer.valueOf(x.split(" ")[3]); i++)
				{
					grid[Integer.valueOf(x.split(" ")[1])][i] = cc[c];
				}
			}
			else
			{
				for (int i = Integer.valueOf(x.split(" ")[1]); i < Integer.valueOf(x.split(" ")[1])+Integer.valueOf(x.split(" ")[3]); i++)
				{
					grid[i][Integer.valueOf(x.split(" ")[2])] = cc[c];
				}
			}c++;
		}
		print();
		
		solve(red);
		solve(red);
	}
	
	static void solve(Block b)
	{
		if (b.p.equals("hori"))
		{
			if (b.l == 3)
			{
				if (isRight(b))
				{
					solve(whoIs(b.x, b.y+b.l));
					moveRight(b);
					return;
				}
				else moveRight(b);
				solve(b);
			}
			else //two
			{
				if (b.x+b.l+1<7)//valid right
				{
					if (isRight(b))
					{
						solve(whoIs(b.x, b.y+b.l));
					}
					//else move
				}
				else if (isLeft(b))
				{
					if (isLeft(b))//WHY DO THIS TWICE?
					{
						solve(whoIs(b.x, b.y-1));
						moveLeft(b);
						return;
					}
					//else move
				}
				else moveLeft(b);
				solve(b);
			}
		}
		else //vert
		{
			if (b.l == 3)
			{
				if (b.downFlag)
				{
					if (!isUp(b) && b.x-1 == 0) {b.downFlag = false;}
					if (isUp(b))
					{
						b.downFlag = false;
						solve(whoIs(b.x-1, b.y));
						moveUp(b);
						return;
					}
					else moveUp(b);//ITS SHOULD RETURN HERE!!!
					solve(b);
				}
				else 
				{
					if (b.x+b.l == 7) {b.downFlag = true; return;}
					if (isDown(b)) 
					{
						b.downFlag = true;
						solve(whoIs(b.x+b.l, b.y));
					}
					else moveDown(b);
					solve(b);
				}
			}
			else //two piece
			{
				
				if (b.x+b.l < 7 && grid[b.x+b.l][b.y] == null)
				{
					//solve(whoIs(b.x+1, b.y));
					moveDown(b);
					return;
				}
				
				if (isUp(b))//questionable
				{
					solve(whoIs(b.x-1, b.y));//PROBLEM IS HERE
					moveUp(b);
					return;
				}
			}
		}
	}
	
	static void moveRight(Block b)
	{
		for (int i = b.y+b.l; i < 7; i++)//there is a space you need to start i from the end of the block
		{
			if (grid[b.x][i] == null)
			{
				grid[b.x][b.y] = null;
				b.y++;
				grid[b.x][b.y+b.l-1] = grid[b.x][b.y];
				print();
			}
			else break;
		}
	}
	
	static void moveLeft(Block b)
	{
		for (int i = b.y-1; i > 0; i--)
		{
			if (grid[b.x][i] == null)
			{
				grid[b.x][b.y+b.l-1] = null;
				grid[b.x][b.y-1] = grid[b.x][b.y];
				b.y--;
				print();
			}
			else break;
		}
	}
	
	static void moveDown(Block b)
	{
		for (int i = b.x+b.l; i < 7; i++)
		{
			if (grid[i][b.y] == null)
			{
				grid[b.x+b.l][b.y] = grid[b.x][b.y];
				grid[b.x][b.y] = null;
				b.x++;
				print();
			}
			else break;
		}
	}
	
	static void moveUp(Block b)
	{
		for (int i = b.x-1; i > 0; i--)
		{
			if (grid[i][b.y] == null)
			{
				grid[i][b.y] = grid[b.x][b.y];
				grid[b.x+b.l-1][b.y] = null;
				b.x--;
				print();
			}
			else break;
		}
	}
	
	static boolean isRight(Block b)
	{
		if (grid[b.x][b.y+b.l] != null) return true;
		else return false;
	}
	
	static boolean isLeft(Block b)
	{
		if (grid[b.x][b.y-1] != null) return true;
		else return false;
	}
	
	static boolean isUp(Block b)
	{
		if (grid[b.x-1][b.y] != null) return true;
		else return false;
	}
	
	static boolean isDown(Block b)
	{
		if (grid[b.x+b.l][b.y] != null) return true;
		else return false;
	}
	
	static Block whoIs(int x, int y)
	{
		for (Block b : blocks)
		{
			if (b.p.equals("vert") && b.y == y && b.x <= x && b.x + b.l - 1 >= x)//y == b.y && b.x + b.l > x && !(b.x > x))//y == b.y && x + y > 3 && x < 4)//vert
			{
				return b;
			}
			else if (b.p.equals("hori") && b.x == x && b.y <= y && b.y + b.l - 1 >= y)//b.x == x && b.y == y)//(b.p.equals("hori") && b.equals(red) && b.x == x && b.y+b.l >= y)//hori
			{
				return b;
			}
		}
		return null;
	}
	
	static void print()
	{
		for (int x = 1; x < 7; x++)
		{
			for (int y = 1; y < 7; y++)
			{
				if (grid[x][y] == null) System.out.print("-");
				else System.out.print(grid[x][y]);
			}
			System.out.println();
		}
		System.out.println();
	}
/*
    ###---
	@-----
	@-----
	@-----
	------
	------
*/
}