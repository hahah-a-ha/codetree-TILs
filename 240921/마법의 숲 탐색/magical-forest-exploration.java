import java.io.*;
import java.util.*;

public class Main {
    static int r;
    static int c;
    static int[][] map;
    static int answer;
    static boolean[][] exitmap;

    public static void mapSet(int cx, int cy, int num) {
        map[cx][cy] = num;
        map[cx - 1][cy] = num;
        map[cx][cy - 1] = num;
        map[cx][cy + 1] = num;
        map[cx + 1][cy] = num;
    }

    public static int[] monster(int x, int y, int exit, int num) {
        int cx = x;
        int cy = y;
        while (true) {
            boolean can = false;
            if (cx < r + 1 && map[cx + 1][cy - 1] == 0 && map[cx + 2][cy] == 0 && map[cx + 1][cy + 1] == 0) {
                mapSet(cx, cy, 0);
                //다시 num으로
                cx += 1;
                mapSet(cx, cy, num);
                can = true;
            } else if (cx < r + 1 && cy >= 2 && map[cx - 1][cy - 1] == 0 && map[cx][cy - 2] == 0 && map[cx + 1][cy - 2] == 0 && map[cx + 1][cy - 1] == 0 && map[cx + 2][cy] == 0) {
                mapSet(cx, cy, 0);
                //다시 num으로
                cx += 1;
                cy -= 1;
                mapSet(cx, cy, num);
                can = true;
                exit = (exit + 3) % 4;
            } else if (cy < c - 2 && cx < r + 1 && map[cx - 1][cy + 1] == 0 && map[cx][cy + 2] == 0 && map[cx + 1][cy + 1] == 0 && map[cx + 2][cy + 1] == 0 && map[cx + 1][cy + 2] == 0) {
                mapSet(cx, cy, 0);
                cx += 1;
                cy += 1;
                mapSet(cx, cy, num);
                can = true;
                exit = (exit + 1) % 4;
            }
            if (!can) break;
        }
        return new int[]{cx, cy, exit};
    }

    public static void moveAngel(int x, int y, int num) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{x, y});
        boolean[] numch = new boolean[1000];
        numch[num] = true;
        boolean[][] visited = new boolean[r + 3][c];
        visited[x][y] = true;
        int score = Integer.MIN_VALUE;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            score = Math.max(score, cur[0]);
            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];

                if (nx >= 0 && nx < r + 3 && ny >= 0 && ny < c && !visited[nx][ny] && (map[nx][ny] == map[cur[0]][cur[1]] || (map[nx][ny] != 0 && exitmap[cur[0]][cur[1]]))) {
                    q.offer(new int[]{nx, ny});
                    visited[nx][ny] = true;
                }
            }
        }
        answer += score - 2;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        exitmap = new boolean[r + 3][c];
        ArrayList<int[]> lists = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            lists.add(new int[]{Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())});
        }

        map = new int[r + 3][c];
        for (int i = 0; i < k; i++) {
            int ax = 1;
            int ay = lists.get(i)[0] - 1;
            //이동
            int[] cur = monster(ax, ay, lists.get(i)[1], i + 1);
            boolean incheck = true;
            //몸의 일부가 숲을 벗어난 상황이라면
            if (cur[0] <= 2) {
                //다 빠져나감.
                for (int j = 0; j < r + 3; j++) {
                    Arrays.fill(map[j], 0);
                    Arrays.fill(exitmap[j], false);
                }
                incheck = false;
            }
            if (incheck) {
                //map에 출구 표시
                if (cur[2] == 0) {
                    exitmap[cur[0] - 1][cur[1]] = true;
                } else if (cur[2] == 1) {
                    exitmap[cur[0]][cur[1] + 1] = true;
                } else if (cur[2] == 2) {
                    exitmap[cur[0] + 1][cur[1]] = true;
                } else {
                    exitmap[cur[0]][cur[1] - 1] = true;
                }
                moveAngel(cur[0], cur[1], i + 1);
            }
        }
        System.out.println(answer);
    }
}