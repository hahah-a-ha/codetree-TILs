import java.io.*;
import java.util.*;


public class Main {
    static class Distance implements Comparable<Distance> {
        int num;
        int x;
        int y;
        int distance;

        Distance(int num, int x, int y, int distance) {
            this.num = num;
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        @Override
        public int compareTo(Distance o) {
            if (this.distance == o.distance) {
                if (this.x == o.x) {
                    return o.y - this.y;
                } else {
                    return o.x - this.x;
                }
            } else {
                return this.distance - o.distance;
            }
        }
    }

    static class Move implements Comparable<Move> {
        int i;
        int distance;

        Move(int x, int y) {
            this.i = x;
            this.distance = y;
        }

        @Override
        public int compareTo(Move o) {
            return this.distance - o.distance;
        }
    }

    static boolean outOfRange(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= n) return true;
        else return false;
    }

    static class SanMove implements Comparable<SanMove> {
        int i;
        int distance;

        SanMove(int x, int y) {
            this.i = x;
            this.distance = y;
        }

        @Override
        public int compareTo(SanMove o) {
            if (this.distance == o.distance) {
                return this.i - o.i;
            } else {
                return this.distance - o.distance;
            }
        }
    }

    static int n;

    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int rx = Integer.parseInt(st.nextToken()) - 1;
        int ry = Integer.parseInt(st.nextToken()) - 1;
        HashMap<Integer, int[]> hm = new HashMap<>(); // key: 산타 번호 value: 산타 위치
        int[] sdx = {-1, 0, 1, 0}; //산타의 이동방향 4가지
        int[] sdy = {0, 1, 0, -1};
        int[][] map = new int[n][n];
        int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1}; //루돌프는 8가지 방향으로 이동함요
        int[] dy = {0, 1, 1, 1, 0, -1, -1, -1}; //루돌프는 8가지 방향으로 이동함요
        int rNum = 40; //map 상에서 루돌프 존재 위함
        int[] santaCheck = new int[p + 1]; //-1은 탈락, 0은 정상, 1이상은 기절.
        Arrays.fill(santaCheck, -1000);
        int[] score = new int[p + 1]; //산타 점수
        for (int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            hm.put(num, new int[]{x, y});
            map[x][y] = num;
        }
        int count = p;
        int i = 1;
        while (i <= m) {
            //루돌프 움직임 시작
            int rcx = rx;
            int rcy = ry;
            PriorityQueue<Distance> pq = new PriorityQueue<>();
            for (int x : hm.keySet()) {
                if (santaCheck[x] == -1) continue; //탈락한 산타라면 무시.
                int dis = (int) Math.pow((rx - hm.get(x)[0]), 2) + (int) Math.pow((ry - hm.get(x)[1]), 2);
                pq.add(new Distance(x, hm.get(x)[0], hm.get(x)[1], dis));
            }
            Distance cur = pq.poll(); //선택된 산타 정보 가지고 있음
            PriorityQueue<Move> pq2 = new PriorityQueue<>();
            for (int j = 0; j < 8; j++) {
                int nx = rx + dx[j];
                int ny = ry + dy[j];
                if (outOfRange(nx, ny)) continue;
                int distance = (int) Math.pow((nx - cur.x), 2) + (int) Math.pow((ny - cur.y), 2);
                pq2.add(new Move(j, distance));
            }
            Move sel = pq2.poll(); //선택된 산타 중에서도 선택된 방향.
            rx += dx[sel.i];
            ry += dy[sel.i]; //루돌프 움직임 끝
            map[rcx][rcy] = 0; //루돌프 원래 위치 0으로
            if (map[rx][ry] > 0) { //루돌프가 움직일 위치에 산타가 있다면
                int sn = map[rx][ry]; //움직일 위치에 있는 산타 번호
                score[sn] += c; //해당 산타 점수 획득함.
                int newSX = rx + dx[sel.i] * c;   //루돌프 이동 방향대로 c만큼 이동
                int newSY = ry + dy[sel.i] * c;
                if (outOfRange(newSX, newSY)) {
                    santaCheck[sn] = -1;
                    count--;// 산타 탈락함.
                    if (count == 0) break;
                    map[rx][ry] = rNum; //루돌프 있게 됨.
                } else {
                    if (map[newSX][newSY] > 0) { //산타 있으면 상호 작용 시작
                        int nnx = newSX;
                        int nny = newSY;
                        while (true) {
                            int origin = map[nnx][nny]; //기존에 있던 산타
                            int orgx = nnx + dx[sel.i];
                            int orgy = nny + dy[sel.i];
                            if (outOfRange(orgx, orgy)) {
                                santaCheck[origin] = -1;
                                count--;
                                if (count == 0) break;
                                break;
                            } else if (map[orgx][orgy] == 0) { //이동해서 또 없으면 종료
                                map[orgx][orgy] = origin;
                                hm.put(origin, new int[]{orgx, orgy});
                                break;
                            } else { //상호 작용 또 발생
                                nnx = orgx;
                                nny = orgy;
                            }
                        }
                    }
                    map[rx][ry] = rNum;
                    hm.put(sn, new int[]{newSX, newSY});
                    map[newSX][newSY] = sn;
                    santaCheck[sn] = i; //충돌했으므로 기절 상태
                }
            } else {
                map[rx][ry] = rNum;
            }
            //산타 이동 시작.
            for (int x : hm.keySet()) {
                if (santaCheck[x] == -1 || santaCheck[x] == i || santaCheck[x] + 1 == i)
                    continue; //탈락했거나, 기절 시 산타 이동 불가
                if (santaCheck[x] + 2 == i) santaCheck[x] = -1000;
                int curDis = (int) Math.pow((rx - hm.get(x)[0]), 2) + (int) Math.pow((ry - hm.get(x)[1]), 2);
                int[] cursanta = hm.get(x);
                PriorityQueue<SanMove> pq3 = new PriorityQueue<>();
                for (int j = 0; j < 4; j++) {
                    int nsx = hm.get(x)[0] + sdx[j];
                    int nsy = hm.get(x)[1] + sdy[j];
                    if (outOfRange(nsx, nsy)) continue;
                    if (map[nsx][nsy] > 0 && map[nsx][nsy] < rNum) continue;
                    int newdis = (int) Math.pow((nsx - rx), 2) + (int) Math.pow((nsy - ry), 2);
                    if (newdis >= curDis) continue;
                    pq3.add(new SanMove(j, newdis));
                }
                if (pq3.size() == 0) continue; //움직일 곳이 없다면 움직이지 않음
                SanMove s = pq3.poll();
                map[cursanta[0]][cursanta[1]] = 0; //원래 있던 곳 0으로
                if (map[cursanta[0] + sdx[s.i]][cursanta[1] + sdy[s.i]] == rNum) { //산타가 움직여서 충돌 발생
                    int scolx = cursanta[0] + sdx[s.i] + d * sdx[(s.i + 2) % 4];
                    int scoly = cursanta[1] + sdy[s.i] + d * sdy[(s.i + 2) % 4];
                    score[x] += d;
                    if (outOfRange(scolx, scoly)) {
                        santaCheck[x] = -1;
                        if (count == 0) break;
                        count--;
                    } else {
                        if (map[scolx][scoly] > 0) { //다른 산타 있으면
                            int nnx = scolx;
                            int nny = scoly;
                            while (true) {
                                int origin = map[nnx][nny];
                                int orgx = nnx + sdx[(s.i + 2) % 4];
                                int orgy = nny + sdy[(s.i + 2) % 4];
                                if (outOfRange(orgx, orgy)) {
                                    santaCheck[origin] = -1;
                                    if (count == 0) break;
                                    count--;
                                    break;
                                } else if (map[orgx][orgy] == 0) { //이동해서 또 없으면 종료
                                    map[orgx][orgy] = origin;
                                    hm.put(origin, new int[]{orgx, orgy});
                                    break;
                                } else { //상호 작용 또 발생
                                    nnx = orgx;
                                    nny = orgy;
                                }
                            }
                        }
                        hm.put(x, new int[]{scolx, scoly});
                        map[scolx][scoly] = x;
                        santaCheck[x] = i;
                    }
                } else {
                    hm.put(x, new int[]{cursanta[0] + sdx[s.i], cursanta[1] + sdy[s.i]});
                    map[cursanta[0] + sdx[s.i]][cursanta[1] + sdy[s.i]] = x;
                }

            }
            for (int z = 1; z <= p; z++) {
                if (santaCheck[z] == -1) continue;
                score[z]++;
            }
            i++;
        }
        for (int j = 1; j <= p; j++) {
            System.out.print(score[j] + " ");
        }
    }
}