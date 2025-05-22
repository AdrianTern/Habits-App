import TaskItem from "./TaskItem";
import { List } from '@mui/material';

function TaskList({ tasks, onToggle }){
    return(
        <List className="task-list">
            {tasks.map(task => (
                <TaskItem key={task.id} task={task} onToggle={onToggle}/>
            ))}
        </List>
    );
}

export default TaskList;