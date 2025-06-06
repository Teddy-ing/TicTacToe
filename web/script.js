const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 1000);
const renderer = new THREE.WebGLRenderer({ antialias: true });
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// Lighting
const ambient = new THREE.AmbientLight(0xffffff, 0.8);
scene.add(ambient);
const directional = new THREE.DirectionalLight(0xffffff, 0.5);
directional.position.set(5, 10, 7);
scene.add(directional);

camera.position.set(0, 5, 8);
camera.lookAt(0, 0, 0);

// Game state
const PLAYER = 1;
const CPU = 2;
let board = Array(9).fill(0); // 0 empty
let cells = [];
const info = document.getElementById('info');

// Create board
const cellSize = 1.5;
const geometry = new THREE.PlaneGeometry(cellSize, cellSize);
const material = new THREE.MeshBasicMaterial({ color: 0x222222, side: THREE.DoubleSide });
for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
        const cellMesh = new THREE.Mesh(geometry, material.clone());
        cellMesh.position.x = (j - 1) * cellSize;
        cellMesh.position.z = (i - 1) * cellSize;
        cellMesh.rotation.x = -Math.PI / 2;
        scene.add(cellMesh);

        const group = new THREE.Group();
        group.position.copy(cellMesh.position);
        scene.add(group);

        cells.push({mesh: cellMesh, group: group});
    }
}

function createX(color) {
    const lineMat = new THREE.MeshLambertMaterial({ color });
    const barGeom = new THREE.BoxGeometry(1, 0.2, 0.2);
    const bar1 = new THREE.Mesh(barGeom, lineMat);
    bar1.rotation.y = Math.PI / 4;
    const bar2 = new THREE.Mesh(barGeom, lineMat);
    bar2.rotation.y = -Math.PI / 4;
    const g = new THREE.Group();
    g.add(bar1);
    g.add(bar2);
    return g;
}

function createO(color) {
    const torusGeom = new THREE.TorusGeometry(0.5, 0.15, 16, 100);
    const mat = new THREE.MeshLambertMaterial({ color });
    const t = new THREE.Mesh(torusGeom, mat);
    t.rotation.x = Math.PI / 2;
    return t;
}

function checkWinner() {
    const combos = [
        [0,1,2], [3,4,5], [6,7,8],
        [0,3,6], [1,4,7], [2,5,8],
        [0,4,8], [2,4,6]
    ];
    for (const c of combos) {
        if (board[c[0]] && board[c[0]] === board[c[1]] && board[c[1]] === board[c[2]]) {
            return board[c[0]];
        }
    }
    if (board.every(v => v !== 0)) return 3; // stalemate
    return 0; // no winner
}

function cpuMove() {
    const empties = board.map((v, i) => v === 0 ? i : -1).filter(i => i !== -1);
    if (empties.length === 0) return;
    const index = empties[Math.floor(Math.random() * empties.length)];
    board[index] = CPU;
    const piece = createO(0x00ff00);
    cells[index].group.add(piece);
}

function onMouseDown(event) {
    event.preventDefault();
    const mouse = new THREE.Vector2(
        (event.clientX / window.innerWidth) * 2 - 1,
        -(event.clientY / window.innerHeight) * 2 + 1
    );
    const raycaster = new THREE.Raycaster();
    raycaster.setFromCamera(mouse, camera);
    const intersects = raycaster.intersectObjects(cells.map(c => c.mesh));
    if (intersects.length > 0) {
        const index = cells.findIndex(c => c.mesh === intersects[0].object);
        if (board[index] !== 0) return; // already taken
        board[index] = PLAYER;
        const piece = createX(0xff0000);
        cells[index].group.add(piece);
        let result = checkWinner();
        if (result === 0) {
            cpuMove();
            result = checkWinner();
        }
        if (result === PLAYER) {
            info.textContent = 'You win!';
            window.removeEventListener('mousedown', onMouseDown);
        } else if (result === CPU) {
            info.textContent = 'CPU wins.';
            window.removeEventListener('mousedown', onMouseDown);
        } else if (result === 3) {
            info.textContent = 'Stalemate.';
            window.removeEventListener('mousedown', onMouseDown);
        }
    }
}

window.addEventListener('mousedown', onMouseDown);

function animate() {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
}
animate();

window.addEventListener('resize', () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
