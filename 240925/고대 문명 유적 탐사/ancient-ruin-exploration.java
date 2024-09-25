import java.io.*;
import java.util.*;


public class Main {
    static int[] dx;
    static int[] dy;

    public static void rotate(int[][] map, int cx, int cy) {
        int[][] temp = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[j][2 - i] = map[cx - 1 + i][cy - 1 + j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                map[cx - 1 + i][cy - 1 + j] = temp[i][j];
            }
        }

    }

    public static int bfs(int x, int y, boolean[][] visited, int num, int[][] map) {
        visited[x][y] = true;
        int count = 1;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < 5 && ny >= 0 && ny < 5 && !visited[nx][ny] && map[nx][ny] == num) {
                count += bfs(nx, ny, visited, num, map);
            }
        }
        return count;
    }

    public static int found(int[][] map) {
        boolean[][] visited = new boolean[5][5];
        int total = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    int count = bfs(i, j, visited, map[i][j], map);
                    if (count >= 3) total += count;
                }
            }
        }
        return total;
    }

    static class Score implements Comparable<Score> {
        int count;
        int angle;
        int x;
        int y;

        Score(int x, int y, int z, int w) {
            this.count = x;
            this.angle = y;
            this.x = z;
            this.y = w;
        }

        @Override
        public int compareTo(Score o) {
            if (this.count != o.count) {
                return o.count - this.count;
            }

            if (this.angle != o.angle) {
                return this.angle - o.angle;
            }

            if (this.x != o.x) {
                return this.x - o.x;
            }
            return this.y - o.y;
        }

    }

    public static void bfsCount(int x, int y, boolean[][] visited, int num, int[][] map, ArrayList<int[]> record) {
        visited[x][y] = true;
        record.add(new int[]{x, y});
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < 5 && ny >= 0 && ny < 5 && !visited[nx][ny] && map[nx][ny] == num) {
                bfsCount(nx, ny, visited, num, map, record);
            }
        }
    }

    static class Point implements Comparable<Point> {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            if (this.y == o.y) {
                return o.x - this.x;
            } else {
                return this.y - o.y;
            }

        }

    }

    public static PriorityQueue<Point> foundCount(int[][] map) {
        boolean[][] visited = new boolean[5][5];
        PriorityQueue<Point> pq = new PriorityQueue<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!visited[i][j]) {
                    ArrayList<int[]> record = new ArrayList<>();
                    bfsCount(i, j, visited, map[i][j], map, record);
                    if (record.size() >= 3) {
                        for (int[] z : record) {
                            pq.add(new Point(z[0], z[1]));
                        }
                    }
                }
            }
        }
        return pq;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int k = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        dx = new int[]{0, 0, 1, -1};
        dy = new int[]{1, -1, 0, 0};
        int[][] map = new int[5][5];
        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int[] nums = new int[m];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }
        int cur = 0;
        int start = 0;
        int[][] saveMap = new int[5][5];
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                saveMap[i][j] = map[i][j];
            }
        }

        while (cur < k) {
            int turnTotal = 0;
            PriorityQueue<Score> pq = new PriorityQueue<>();
            int[][] middles = {{1, 1}, {1, 2}, {1, 3}, {2, 1}, {2, 2}, {2, 3}, {3, 1}, {3, 2}, {3, 3}};
            for (int i = 0; i < 9; i++) {
                int[][] copyMap = new int[5][5];
                for (int l = 0; l < 5; l++) {
                    for (int r = 0; r < 5; r++) {
                        copyMap[r][l] = saveMap[r][l];
                    }
                }
                int cx = middles[i][0];
                int cy = middles[i][1];
                rotate(copyMap, cx, cy);
                int num_90 = found(copyMap);
                pq.add(new Score(num_90, 90, cx, cy));
                rotate(copyMap, cx, cy);
                int num_180 = found(copyMap);
                pq.add(new Score(num_180, 180, cx, cy));
                rotate(copyMap, cx, cy);
                int num_270 = found(copyMap);
                pq.add(new Score(num_270, 270, cx, cy));
            }
            Score sel = pq.poll();
            if (sel.count == 0) break;
            int[][] zmap = new int[5][5];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    zmap[i][j] = saveMap[i][j];
                }
            }
            turnTotal += sel.count;
            if (sel.angle == 90) {
                rotate(zmap, sel.x, sel.y);
            } else if (sel.angle == 180) {
                rotate(zmap, sel.x, sel.y);
                rotate(zmap, sel.x, sel.y);
            } else {
                rotate(zmap, sel.x, sel.y);
                rotate(zmap, sel.x, sel.y);
                rotate(zmap, sel.x, sel.y);
            }

            while (true) {
                PriorityQueue<Point> change = foundCount(zmap);
                int num = change.size();
                for (int i = start; i < start + num; i++) {
                    Point p = change.poll();
                    zmap[p.x][p.y] = nums[i];
                }
                int curCount = found(zmap);
                start += num;
                turnTotal += curCount;
                if (curCount == 0) break;
            }
            if (turnTotal == 0) break;
            System.out.print(turnTotal + " ");
            cur++;
            for(int i=0; i<5; i++) {
                for(int j=0; j<5; j++) {
                    saveMap[i][j] = zmap[i][j];
                }
            }
        }

    }
}