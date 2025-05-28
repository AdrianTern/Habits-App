import dayjs from 'dayjs';

export const isTaskMatchFilter =(task, filter) => {
    const dueDate = dayjs(task.dueDate);
    const today  = dayjs();

    switch(filter){
        case 'today':
            return dueDate == today;
        case 'upcoming':
            return dueDate > today;
        case 'overdue':
            return dueDate < today && !task.isCompleted;
        case 'all':
            return dueDate >= today;
        default:
            return true;
    }
}